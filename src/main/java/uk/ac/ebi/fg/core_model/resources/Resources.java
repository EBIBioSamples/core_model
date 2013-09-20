package uk.ac.ebi.fg.core_model.resources;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import uk.ac.ebi.fg.persistence.dao.hibernate.utils.DbSchemaEnhancer;

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
	private EntityManagerFactory entityManagerFactory = null;
	
	private static Resources instance = new Resources ();
	
	private Resources () {
	}

	public static Resources getInstance () {
		return instance;
	}
	
	/**
	 * The wrapper to the database. This works via Hibernate, so you need to put some hibernate.properties in the 
	 * Java classpath. See the Maven structure for examples.
	 * 
	 */
	public EntityManagerFactory getEntityManagerFactory ()
	{
		if ( entityManagerFactory != null ) return entityManagerFactory;

		entityManagerFactory = Persistence.createEntityManagerFactory ( "defaultPersistenceUnit" );
		new DbSchemaEnhancer ().enhance ( entityManagerFactory );
		return entityManagerFactory;
		
		// return entityManagerFactory = Persistence.createEntityManagerFactory ( "defaultPersistenceUnit" );
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
