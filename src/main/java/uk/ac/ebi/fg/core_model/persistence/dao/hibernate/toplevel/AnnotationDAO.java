package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.metadata.ClassMetadata;

import uk.ac.ebi.fg.core_model.toplevel.Annotatable;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>26 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class AnnotationDAO extends IdentifiableDAO<Annotation>
{
	
	public AnnotationDAO ( Class<Annotation> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}

	
	public int purge ()
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
	}
	
}
