package uk.ac.ebi.fg.persistence.hibernate.schema_enhancer;

import java.util.Map;
import java.util.ServiceLoader;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.utils.collections.ArraySearchUtils;



/**
 * Calls all the instances of {@link DbSchemaEnhancer} that can be found in the classpath via the 
 * <a href = 'http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html'>SPI mechanism</a>.
 *
 * <dl><dt>date</dt><dd>19 Sep 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DbSchemaEnhancerProcessor
{
	public Logger log = LoggerFactory.getLogger ( this.getClass () );
	
	protected final EntityManagerFactory entityManagerFactory;

	public DbSchemaEnhancerProcessor ( EntityManagerFactory entityManagerFactory )
	{
		super ();
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * Finds and trigger the available {@link DbSchemaEnhancer}s if hibernate.connection.driver_class = *Oracle* and
	 * hibernate.hbm2ddl.auto = create|update.
	 */
	public void enhance ()
	{
		Map<String, Object> props = entityManagerFactory.getProperties ();
		
		// TODO: Are there more DBs the jerk doesn't work well with?!
		if ( !StringUtils.trimToEmpty ( (String) props.get ( "hibernate.connection.driver_class" ) ).contains ( "Oracle" ) )
			return;
		
		if ( !ArraySearchUtils.isOneOf ( (String) props.get ( "hibernate.hbm2ddl.auto" ), "create", "update" )) return;
		
		ServiceLoader<DbSchemaEnhancer> enhancers = ServiceLoader.load ( DbSchemaEnhancer.class );
		for ( DbSchemaEnhancer enhancer: enhancers ) 
			enhancer.enhance ( this.entityManagerFactory );
	}
}
