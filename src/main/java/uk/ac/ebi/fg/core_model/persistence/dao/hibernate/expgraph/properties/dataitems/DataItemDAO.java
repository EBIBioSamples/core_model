package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.expgraph.properties.dataitems;

import javax.persistence.EntityManager;

import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.DataItem;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.AnnotatableDAO;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>26 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DataItemDAO extends AnnotatableDAO<DataItem>
{
	public DataItemDAO ( Class<DataItem> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}

	public int purge ()
	{
		EntityManager em = this.getEntityManager ();
		int result = em.createQuery ( 
			"DELETE FROM DataItem di WHERE di.id NOT IN (\n" +
			"  SELECT jdi.id FROM ExperimentalPropertyValue pv JOIN pv.dataItems jdi )" ).executeUpdate ();
		return result;
	}
}
