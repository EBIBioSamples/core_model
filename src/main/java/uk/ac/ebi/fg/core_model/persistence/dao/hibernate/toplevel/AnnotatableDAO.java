package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.lang.Validate;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.metadata.ClassMetadata;

import uk.ac.ebi.fg.core_model.toplevel.Annotatable;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>17 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class AnnotatableDAO<A extends Annotatable> extends IdentifiableDAO<A>
{

	public AnnotatableDAO ( Class<A> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}


	/**
	 * When true, deletes all {@link Annotation} that are not linked to any instance of {@link #getManagedClass()}.
	 * 
	 * Note that is is mainly useful for testing purposes, it is usually more efficient to have a big query cleaning 
	 * up orphan annotations by listing all joined tables at SQL level (see the BioSD loader).
	 */
	public boolean delete ( A annotatable, boolean cascade )
	{
    Validate.notNull ( annotatable, "Internal error: 'entity' must not be null" );
    
    EntityManager em = this.getEntityManager ();
    if ( !em.contains ( annotatable ) ) return false;
    
    if ( cascade ) em.createQuery ( 
    		"DELETE FROM Annotation a WHERE a.id IN ( \n" +
    		"  SELECT ja.id FROM " + getManagedClass ().getName ()  + " an JOIN an.annotations ja )"
    	).executeUpdate ();
    
    em.remove ( annotatable );
    return true;
	}
	
	/*public int purge ()
	{
		EntityManager em = this.getEntityManager ();
		EntityManagerFactory emf = em.getEntityManagerFactory ();
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) emf).getSessionFactory ();

		String hqlf = "DELETE FROM Annotation a WHERE a.id NOT IN ( " +
			"  SELECT ja.id FROM %s an JOIN an.annotations ja)";
		int result = 0;
		for ( ClassMetadata cmeta: sessionFact.getAllClassMetadata ().values () )
		{
			Class<?> mappedClass = cmeta.getMappedClass ();
			if ( !Annotatable.class.isAssignableFrom ( mappedClass ) ) continue;

			result += em.createQuery ( String.format ( hqlf, mappedClass.getCanonicalName () ) ).executeUpdate ();
		}
		
		return result;
	}*/
	
	public int purge ()
	{
		EntityManager em = this.getEntityManager ();
		int result = em.createQuery ( 
			"DELETE FROM DataItem di WHERE di.id NOT IN (\n" +
			"  SELECT jdi.id FROM ExperimentalPropertyValue pv JOIN pv.dataItems jdi )" ).executeUpdate ();
		return result;
	}
}
