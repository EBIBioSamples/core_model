package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.expgraph.properties.dataitems;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.DataItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.DateItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.DateRangeItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.NumberItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.NumberRangeItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.RangeItem;
import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.ValueItem;
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

	@SuppressWarnings ( "unchecked" )
	public <D extends DataItem> List<D> find ( D di, boolean getFirstOnly )
	{
		String qref = null;
		ValueItem<?> val = null;
		RangeItem<?> range = null;
		
		if ( di instanceof NumberItem ) {
			qref = "numberItem.find"; val = (ValueItem<?>) di; 
		}
		else if ( di instanceof DateItem ) {
			qref = "dateItem.find"; val = (ValueItem<?>) di; 
		}
		else if ( di instanceof NumberRangeItem ) {
			qref = "numberRangeItem.find"; range = (RangeItem<?>) di;
		}
		else if ( di instanceof DateRangeItem ) {
			qref = "dateRangeItem.find"; range = (RangeItem<?>) di;
		}
		else throw new RuntimeException ( 
			"Internal error: DataItem DAO cannot deal with the type " + di.getClass ().getName () 
		);
				
		Query q = this.getEntityManager ().createNamedQuery ( qref );

		if ( val != null ) 
			q.setParameter ( "value", val.getValue () );
		else
			q.setParameter ( "low", range.getLow () )
			 .setParameter ( "hi", range.getHi () );		
		
		
		if ( getFirstOnly ) q.setMaxResults ( 1 );

		return q.getResultList ();
	}
	
	public <D extends DataItem> D find ( D di )
	{
		List<D> results = find ( di, true );
		return results.isEmpty () ? null : results.iterator ().next ();
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
