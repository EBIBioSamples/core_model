package uk.ac.ebi.fg.core_model.resources;

import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import uk.ac.ebi.fg.persistence.hibernate.schema_enhancer.DbSchemaEnhancer;
import uk.ac.ebi.fg.persistence.hibernate.schema_enhancer.DefaultDbSchemaEnhancer;
import uk.ac.ebi.utils.collections.ArraySearchUtils;

/**
 * <p>The resources used by this application, such as the {@link #getEntityManagerFactory() entity manager factory}, i.e., 
 * the connection to the database.</p>
 * 
 * <p>{@link LocalContainerEntityManagerFactoryBean} is used to instantiate Hibernate (see below), so you don't need
 * persistence.xml any more, just to set proper connection and other Hibernate parameters in hibernate.properties.</p>
 * 
 * <p>Resources can be customised by extending this class and declaring your custom extension via the SPI mechanism, 
 * i.e., {@link Resources#getInstance()} will look at the classpath, for files named 
 * {@code META-INF/uk.ac.ebi.fg.core_model.resources.Resources}, and will return the class named in such file that has highest
 * {@link Resources#getPriority() priority} defined in such files (see biosd_model for an example).</p> 
 *
 * <dl><dt>date</dt><dd>Aug 2, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class Resources
{
	private EntityManagerFactory entityManagerFactory = null;
	
	private final static Resources instance;

	static
	{
		synchronized ( Resources.class )
		{
			Logger log = LoggerFactory.getLogger ( Resources.class );
			
			// SPI returns all the services in undetermined order, we pick up the top extension by means of the priority
			// property.
			// 
			int maxPriority = 0;
			Resources maxPriorityResources = null;
			
			for ( Resources res: ServiceLoader.load ( Resources.class ) )
			{
				log.debug ( 
					"Found FG Model Resource class  {}, priority: {}", res.getClass ().getCanonicalName (), res.getPriority () 
				);
	
				if ( maxPriorityResources == null ) {
					maxPriority = res.getPriority ();
					maxPriorityResources = res;
				}
				else if ( res.getPriority () > maxPriority ) {
					maxPriority = res.getPriority ();
					maxPriorityResources = res;
				}
			}
			
			instance = maxPriorityResources == null ? new Resources () : maxPriorityResources; 
			log.debug ( "Picking FG Model Resource class = {}", instance.getClass ().getCanonicalName () );
		}
	}
	
	
	/**
	 * SPI requires this to be public, but you should never instance thiese classes directly, use 
	 * {@link #getInstance()} instead.
	 * 
	 */
	public Resources () {
	}


	/**
	 * This returns the top-{@link Resources#getPriority() priority} top-priority {@link Resources} subclass that 
	 * is defined in SPI files (see above).
	 */
	public static Resources getInstance () 
	{
		if ( instance == null ) throw new IllegalStateException ( 
			"Internal error: couldn't initialise application resource handler"
		);
		return instance;
	}

	/**
	 * The priority of this resource class. Returns 0 by default, your extension having biggest priority (and defined
	 * in the SPI file in META-INF/, and available in the classpath), will be the one picked up by {@link #getInstance()}.
	 * 
	 * Hence, a typical way to override this method is: {@code return super.getPriority () + 10}
	 * See biosd_model for examples.
	 */
	public int getPriority () {
		return 0;
	}

	/**
	 * The packages that {@link LocalContainerEntityManagerFactoryBean} will scan to get JPA entity classes.
	 * You need to add your model classes to JPA by means of this method (and custom Resources class).
	 * 
	 * A typical way to override this method is:
	 * <code>return {@link ArrayUtils#add(Object[], Object) add}.( super.getPackagesToScan (), 0, "uk.ac.ebi.fg.biosd.annotator.model.**" )</code>
	 *  
	 */
	public String[] getPackagesToScan () {
		return new String [] { "uk.ac.ebi.fg.core_model.**.*" };
	}
	
	/**
	 * {@link DbSchemaEnhancer Schema enhancers} are invoked by {@link #getEntityManagerFactory(String, Map)}, 
	 * when hibernate.hbm2ddl.auto is "create" or "update", and after having let hibernate to initialise the schema
	 * based on JPA annotations. 
	 */
	public DbSchemaEnhancer[] getDbSchemaEnhancers () {
		return new DbSchemaEnhancer[] { new DefaultDbSchemaEnhancer () };
	}

	/**
	 * This is usually "ebiFgModelsPersistenceUnit" and, as usually, you shouldn't need to change it, if you're using 
	 * only one database. One case where using different persistence unit names might be useful is when you want to
	 * use two different extensions of this core_model (or a derived one), and such extensions go to different databases,
	 * with different schemas.
	 *  
	 */
	public String getDefaultPersistenceUnitName () {
		return "ebiFgModelsPersistenceUnit";
	}
	
	
	/**
	 * Uses "defaultPersistenceUnit" and null as property object, i.e., takes the properties from an hibernate.properties 
	 * in the class path, by calling Hibernate facilities.
	 */
	public EntityManagerFactory getEntityManagerFactory ()
	{
		return getEntityManagerFactory ( (Map<String, String>) null );
	}

	/**
	 * Uses a null property object, i.e., takes the properties from an hibernate.properties in the class path, by calling
	 * Hibernate facilities. 
	 */
	public EntityManagerFactory getEntityManagerFactory ( String persistenceUnitName )
	{
		return getEntityManagerFactory ( persistenceUnitName, null );
	}
	
	/**
	 * Uses {@link #getDefaultPersistenceUnitName()}. 
	 */
	public EntityManagerFactory getEntityManagerFactory ( Map<String, String> properties )
	{
		return getEntityManagerFactory ( getDefaultPersistenceUnitName (), properties );
	}

	
	/**
	 * The wrapper to the database. If not already done creates an {@link EntityManagerFactory}, using
	 * {@link #getPackagesToScan()} and taking properties like the JDBC connection parameters from the
	 * properties method parameter. If such parameter is null, it takes properties the usual way, i.e., 
	 * looking for hibernate.properties in the classpath.
	 * 
	 */
	public synchronized EntityManagerFactory getEntityManagerFactory ( 
		String persistenceUnitName, Map<String, String> properties )
	{
		if ( this.entityManagerFactory != null ) return this.entityManagerFactory;

		// Spring scans JPA annotations across multiple .jars, very convenient, no persistence.xml needed
		LocalContainerEntityManagerFactoryBean springEmf = new LocalContainerEntityManagerFactoryBean ();

		springEmf.setPackagesToScan ( getPackagesToScan () );
		
		springEmf.setPersistenceUnitName ( persistenceUnitName );
		springEmf.setJpaDialect ( new HibernateJpaDialect () );
		springEmf.setPersistenceProviderClass ( HibernatePersistenceProvider.class );
		
		if ( properties != null )
		{
			Properties propertiesConverted = new Properties ();
			for ( String key: properties.keySet () )
				propertiesConverted.setProperty ( key, properties.get ( key ) );
			springEmf.setJpaProperties ( propertiesConverted );
		}
		
		springEmf.afterPropertiesSet ();
		this.entityManagerFactory = springEmf.getObject ();
		
		// Add indices and other improvements to the database schema. See the code inside this class for details.
		this.enhanceDbSchema ();
		
		return this.entityManagerFactory;
	}
	
	/**
	 * See {@link #getDbSchemaEnhancers()}.
	 */
	protected void enhanceDbSchema ()
	{
		Map<String, Object> props = this.entityManagerFactory.getProperties ();
		
		// TODO: Are there more DBs the jerk doesn't work well with?!
		if ( !StringUtils.trimToEmpty ( (String) props.get ( "hibernate.connection.driver_class" ) ).contains ( "Oracle" ) )
			return;
		
		if ( !ArraySearchUtils.isOneOf ( (String) props.get ( "hibernate.hbm2ddl.auto" ), "create", "update" )) return;
		
		for ( DbSchemaEnhancer enhancer: getDbSchemaEnhancers () ) 
			enhancer.enhance ( this.entityManagerFactory );
	}
	
	
	/**
	 * Reset the application entity manager factory, so that a call to 
	 * {@link #getEntityManagerFactory(String, Map)} (and its variants) will recreate a new EMF.
	 * 
	 */
	public synchronized void reset () 
	{
    if ( entityManagerFactory != null && entityManagerFactory.isOpen() ) entityManagerFactory.close();
    entityManagerFactory = null;
	}
	
	/**
	 * closes the {@link #entityManagerFactory}
	 */
	@Override
	protected void finalize () throws Throwable
	{
		reset ();
	}

}
