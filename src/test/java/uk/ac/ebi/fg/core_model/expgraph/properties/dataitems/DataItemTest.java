package uk.ac.ebi.fg.core_model.expgraph.properties.dataitems;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.expgraph.properties.BioCharacteristicValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.ExperimentalPropertyValue;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.expgraph.properties.dataitems.DataItemDAO;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.AnnotatableDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>25 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DataItemTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );
	
	
	@Test
	public void testBasics ()
	{
		NumberItem n1 = new NumberItem ( 2.5 ), n2 = new NumberItem ( 3.5 ), n3 = new NumberItem ( n1.getValue () );
		
		assertEquals ( "n1.equals( n3 ) fails!", n1, n3 );
		assertFalse ( "!n2.equals( n1 ) fails!", n2.equals ( n1 ) );
		
		Set<NumberItem> nset = new HashSet<NumberItem> ();
		nset.addAll ( Arrays.asList ( new NumberItem[] { n1, n2, n3 } ) );
		assertEquals ( "hashCode() seems to fail!", 2, nset.size () );

		DateRangeItem dr1 = new DateRangeItem ( 
			new DateTime ( 2013, 1, 1, 0, 0 ).toDate (), new DateTime ( 2014, 12, 31, 0, 0 ).toDate () 
		);
		DateRangeItem dr2 = new DateRangeItem ( dr1.getLow (), dr1.getHi () );

		assertEquals ( "dr2.equals( dr1 ) fails!", dr2, dr1 );

		Set<DateRangeItem> dset = new HashSet<DateRangeItem> ();
		dset.addAll ( Arrays.asList ( new DateRangeItem[] { dr1, dr2 } ) );
		assertEquals ( "hashCode() seems to fail!", 2, nset.size () );
	}
	
	@Test
	@SuppressWarnings ( "unchecked" )
	public void testPersistence ()
	{
		NumberItem n1 = new NumberItem ( 2.5 ), n2 = new NumberItem ( 3.5 ), n3 = new NumberItem ( n1.getValue () );
		DateRangeItem dr1 = new DateRangeItem ( 
			new DateTime ( 2013, 1, 1, 0, 0 ).toDate (), new DateTime ( 2014, 12, 31, 0, 0 ).toDate () 
		);
		DateRangeItem dr2 = new DateRangeItem ( dr1.getLow (), dr1.getHi () );
	
		EntityManager em = emProvider.getEntityManager ();
		AnnotatableDAO<DataItem> dao = new AnnotatableDAO<DataItem> ( DataItem.class, em );
		
		DataItem[] dataItems = new DataItem [] { n1, n2, n3, dr1, dr2 };
		
		EntityTransaction ts = em.getTransaction ();
		ts.begin ();
		for ( DataItem di: dataItems ) dao.create ( di );
		ts.commit ();

		for ( DataItem di: dataItems )
			assertNotNull ( "Seems that " + di + " is not saved!", di.getId ()  );
		
		dao.setEntityManager ( em = emProvider.newEntityManager () );
		List<DataItem> dbItems = em.createQuery ( "from DataItem" ).getResultList ();
		assertEquals ( "DataItems reloading fails!", dataItems.length, dbItems.size () );
		
		// Clean-up
		ts = em.getTransaction ();
		ts.begin ();
		for ( DataItem di: dbItems ) dao.delete ( di );
		ts.commit ();
	}
	
	@Test
	public void testPropertyValues ()
	{
		NumberItem n = new NumberItem ( 2.5 );
		NumberRangeItem nr = new NumberRangeItem ( 0d, 1d ); 

		BioCharacteristicValue pv = new BioCharacteristicValue ( "1.2 umol", null );
		pv.addDataItem ( n );
		pv.addDataItem ( nr );
		
		EntityManager em = emProvider.getEntityManager ();
		AnnotatableDAO<BioCharacteristicValue> pvdao = new AnnotatableDAO<BioCharacteristicValue> ( BioCharacteristicValue.class, em );
		
		EntityTransaction ts = em.getTransaction ();
		ts.begin ();
		pvdao.create ( pv );
		ts.commit ();
		
		assertNotNull ( "n wasn't saved!", n );
		assertNotNull ( "nr wasn't saved!", nr );
		
		DataItemDAO dataDao = new DataItemDAO ( DataItem.class, em );

		long nid = n.getId (), nrid = nr.getId ();
		ts = em.getTransaction ();
		ts.begin ();
		pvdao.delete ( pv );
		dataDao.delete ( n );
		ts.commit ();
		
		dataDao.setEntityManager ( em = emProvider.newEntityManager () );
		assertNull ( "n deletion didn't work!", dataDao.find ( nid ) );
		
		ts = em.getTransaction ();
		ts.begin ();
		int purgedItems = dataDao.purge ();
		ts.commit ();

		assertTrue ( "data purge didn't work!", purgedItems > 0 );
		assertNull ( "data purge didn't work!", dataDao.find ( nrid ) );
	}
	
	
}
