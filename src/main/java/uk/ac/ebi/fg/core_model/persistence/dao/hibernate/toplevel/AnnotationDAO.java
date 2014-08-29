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
public class AnnotationDAO<AN extends Annotation> extends IdentifiableDAO<AN>
{
	
	public AnnotationDAO ( Class<AN> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}

	
	public int purge ()
	{
		EntityManager em = this.getEntityManager ();
		EntityManagerFactory emf = em.getEntityManagerFactory ();
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) emf).getSessionFactory ();

		String hql = "DELETE FROM Annotation a WHERE\n";
		String exclf = "a NOT IN (SELECT ja.id FROM %s an JOIN an.annotations ja)\n";
		String sep = "  ";
				
		for ( ClassMetadata cmeta: sessionFact.getAllClassMetadata ().values () )
		{
			Class<?> mappedClass = cmeta.getMappedClass ();
			if ( !Annotatable.class.isAssignableFrom ( mappedClass ) ) continue;

			hql += sep + String.format ( exclf,  mappedClass.getCanonicalName () ) + "\n";
			sep = "  AND ";
		}

		return em.createQuery ( hql ).executeUpdate ();
	}
	
}
