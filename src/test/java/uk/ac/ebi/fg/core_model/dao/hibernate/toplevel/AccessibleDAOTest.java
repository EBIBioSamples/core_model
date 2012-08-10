/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.toplevel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class AccessibleDAOTest
{	
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private AccessibleDAO<MyAccessible> dao;
	private EntityManager em;
	
	private String testAcc = "tests.myacc-1";
	
	@Before
	public void init()
	{
		dao = new AccessibleDAO<MyAccessible> ( MyAccessible.class, emProvider.getEntityManager () );
		em = emProvider.getEntityManager ();
		em.remove ( new MyAccessible ( testAcc ) );
		assertFalse ( "Test accessible not removed from the DB!", dao.exists ( testAcc ) );
	}

	@Test
	public void testBasicCreation ()
	{
		MyAccessible acc = new MyAccessible ( testAcc );
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		dao.create ( acc );
		tns.commit ();
		
		assertNotNull ( "Persisted accessible has a null ID!", acc.getId () );
		
		MyAccessible accDB = dao.find ( testAcc );

		assertNotNull ( "Cannot find persisted accessible!", accDB );
		assertEquals ( "Bad accession for retrieved entity!", testAcc, accDB.getAcc () );
	}
}
