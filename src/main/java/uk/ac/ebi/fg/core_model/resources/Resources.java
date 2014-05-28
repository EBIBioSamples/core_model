package uk.ac.ebi.fg.core_model.resources;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import uk.ac.ebi.fg.persistence.hibernate.schema_enhancer.DbSchemaEnhancerProcessor;

/**
 * 
 * The resources used by this application, such as the {@link #getEntityManagerFactory() entity manager factory}, i.e., 
 * the connection to the database.
 *
 * <dl><dt>date</dt><dd>Aug 2, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class Resources
{
	public static final String CUSTOM_RESOURCES_CLASS_PROPERTY = "uk.ac.ebi.fg.models.resourcesClassName";
	
	private EntityManagerFactory entityManagerFactory = null;
	
	private final static Resources instance;

	static 
	{
		String resClassName = System.getProperty ( CUSTOM_RESOURCES_CLASS_PROPERTY );
		
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
		return getEntityManagerFactory ( "ebiFgModelsPersistenceUnit", null );
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

		entityManagerFactory = Persistence.createEntityManagerFactory ( persistenceUnitName, properties );
		
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
