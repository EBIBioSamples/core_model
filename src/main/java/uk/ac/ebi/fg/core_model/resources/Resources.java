package uk.ac.ebi.fg.core_model.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import uk.ac.ebi.fg.persistence.hibernate.schema_enhancer.DbSchemaEnhancerProcessor;

/**
 * <p>The resources used by this application, such as the {@link #getEntityManagerFactory() entity manager factory}, i.e., 
 * the connection to the database.</p>
 * 
 * <p>{@link LocalContainerEntityManagerFactoryBean} is used to instantiate Hibernate (see below), so you don't need
 * persistence.xml any more, just to set proper conncetion and other Hibernate parameters in hibernate.properties.</p>
 * 
 * <p>This class can be customised by placing a file named /fg_models.properties on top of your classpath, usually 
 * inside a Jar that extends this core_model (on top-level of it, which is achieved by using /src/main/resources in 
 * Maven). Such file can contain the following properties:</p>
 * 
 * <dl>
 *   <dt>uk.ac.ebi.fg.models.packages</dt>
 *   <dd>A comma-separated list of packages to scan for looking at JPA entities. This is split into an array and passed
 *   to LocalContainerEntityManagerFactoryBean, to build a correct entity manager factory. If your model is an extension
 *   of this core model (why are you using this module, if not?) you <strong>do need</strong> to list uk.ac.ebi.fg.core_model.**.*
 *   in this parameter (this is the default if it's not specified at all). As you see, you can use the common syntax for
 *   any name at a given level ('*'), or any sublevel ('**').</dd>
 *   
 *   <dt>uk.ac.ebi.fg.models.persistenceUnit</dt>
 *   <dd>The name of the JPA/Hibernate persistence unit, used to create the EntityManager factory via 
 *   {@link #getEntityManagerFactory(String, Map)}. The default for this is 'ebiFgModelsPersistenceUnit' and you should
 *   be fine with it in most cases, ie, when your model is a single-lineage extension of this core. For instance, if 
 *   your model A is an extension of core and you want a database containing the merge of the two, you don't need
 *   to change persistence unit name. That's true for any chain of A1, A2.. An, where An extends A(n-1) and A1 extends
 *   this core. Instead, if you have independent models A and B, both extending the core and both mapping distinct 
 *   databases, you'll need two different persistence units.</dd>
 *   
 *   <dt>uk.ac.ebi.fg.models.resourceClass</dt>
 *   <dd>If you don't like the way this class works, you can extend it and make your application to load your custom
 *   version, by means of this property. your implementation will be given by {@link #getInstance()}. (TODO: we plan
 *   to change this slightly messed approach with Spring beans). While we don't see extreme need for this, in case you 
 *   do it, we recommend you start from this class {@link #getEntityManagerFactory(String, Map)} in particular, where
 *   there is code to use Spring to instantiate Hibernate and to fix the schema via {@link DbSchemaEnhancerProcessor}.
 *   </dd>
 * </dl>
 * 
 *
 * <dl><dt>date</dt><dd>Aug 2, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class Resources
{
	private EntityManagerFactory entityManagerFactory = null;
	private static Properties resourceProperties = null;
	
	private final static Resources instance;

	static
	{
		resourceProperties = new Properties ();
		InputStream repPropIn = Resources.class.getResourceAsStream ( "/fg_models.properties" );
		try {
			if ( repPropIn != null ) resourceProperties.load ( new InputStreamReader ( repPropIn ) );
		}
		catch ( IOException ex ) {
			throw new RuntimeException ( 
				"Internal error: cannot read from internal resource /fg_models.properties, due to: " + ex.getMessage (), 
				ex 
			);
		}
		
		String resClassName = resourceProperties.getProperty ( "uk.ac.ebi.fg.models.resourceClass", null );
		
		if ( resClassName == null )
		{
			instance = new Resources ();
		}
		else 
		{
			@SuppressWarnings ( "rawtypes" )
			Class resClass = null;
			Resources result = null;
			try
			{
				resClass = Class.forName ( resClassName );
				result = (Resources) resClass.newInstance ();
			} catch ( ClassNotFoundException ex )
			{
				// TODO: proper exception
				throw new RuntimeException ( String.format ( 
					  "Internal error while initialising application resource handler, class '%s' not found: %s", 
					  resClass,
					  ex.getMessage ()
					),
					ex 
				);
			} 
			catch ( InstantiationException ex ) 
			{
				throw new RuntimeException ( 
					"Internal error while initialising application resource handler: " + ex.getMessage (), ex );
			} 
			catch ( IllegalAccessException ex )
			{
				throw new RuntimeException ( 
					"Internal error while initialising application resource handler: " + ex.getMessage (), ex );
			}
			finally {
				instance = result;
			}
		}
	}
	
	
	private Resources () {
	}

	/**
	 * This can be affected by the property {@link #CUSTOM_RESOURCES_CLASS_PROPERTY}, to customise the Resources class
	 * that is actually used. 
	 */
	public static Resources getInstance () 
	{
		if ( instance == null ) throw new IllegalStateException ( 
			"Internal error: couldn't initialise application resource handler"
		);
		return instance;
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
	 * Uses 'ebiFgModelsPersistenceUnit' as persistence unit name. This should be used for all the models
	 * extending core_model. Possibly you need to redefine your own persistence.xml for such extended models, see 
	 * the project biosd_model/ for details (in particular how the JPA Maven plug-in is used to scan all the classes
	 * automatically).
	 * 
	 * While this method to get an EMF is flexible enough in most situation, there might still be cases where you need
	 * something more advanced . 
	 * 
	 */
	public EntityManagerFactory getEntityManagerFactory ( Map<String, String> properties )
	{
		String persistenceUnitName = resourceProperties.getProperty ( 
			"uk.ac.ebi.fg.models.persistenceUnit", "ebiFgModelsPersistenceUnit" 
		);
		return getEntityManagerFactory ( persistenceUnitName, null );
	}

	
	/**
	 * The wrapper to the database. If not already done creates an {@link EntityManagerFactory}, by looking up 
	 * a persistence.xml file that defines a persistence unit named persistenceUnitName. It also passes the properties
	 * parameter to Hibernate. See Hibernate documentation for more information.
	 * 
	 * While this method to get an EMF is flexible enough in most situation, there might still be cases where you need
	 * something more advanced. For instance, Hibernate doesn't support the merging of multiple persistence.xml, which
	 * can be done via <a href = 'http://tinyurl.com/k44hyxk'>Spring</a>. 
	 * 
	 */
	public EntityManagerFactory getEntityManagerFactory ( String persistenceUnitName, Map<String, String> properties )
	{
		if ( entityManagerFactory != null ) return entityManagerFactory;

		// Spring scans JPA annotations across multiple .jars, very convenient, no persistence.xml needed
		LocalContainerEntityManagerFactoryBean springEmf = new LocalContainerEntityManagerFactoryBean ();

		String packagesToScan = resourceProperties.getProperty ( 
			"uk.ac.ebi.fg.models.packages", "uk.ac.ebi.fg.core_model.**.*" 
		);
		springEmf.setPackagesToScan ( packagesToScan.split ( "," ) );
		
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
		entityManagerFactory = springEmf.getObject ();
		
		// Add indices and other improvements to the database schema. See the code inside this class for details.
		new DbSchemaEnhancerProcessor ( entityManagerFactory ).enhance ();
		
		return entityManagerFactory;
	}

	/**
	 * closes the {@link #entityManagerFactory}
	 */
	@Override
	protected void finalize () throws Throwable
	{
    if ( entityManagerFactory != null && entityManagerFactory.isOpen() )
    	entityManagerFactory.close();
	}
}
