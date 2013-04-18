/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.terms.CVTerm;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * 
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 22, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class CVTermDAOTest
{	
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private CVTermDAO<AnnotationType> dao;
	private EntityManager em;
	
	private String testName = "tests, my name 1";
	
	@Before
	public void init()
	{
		em = emProvider.getEntityManager ();
		dao = new CVTermDAO<AnnotationType> ( AnnotationType.class, em );
	}
	
	@After
	public void cleanUpDB ()
	{
		AnnotationType cvDB = dao.find ( testName );
		if ( cvDB != null ) {
			EntityTransaction tns = em.getTransaction ();
			tns.begin ();
			dao.delete ( cvDB );
			tns.commit ();
		}
		assertFalse ( "Test cvterm not removed from the DB!", dao.contains ( testName ) );
	}

	@Test
	public void testBasicCreation ()
	{
		AnnotationType cvt = new AnnotationType ( testName );
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		dao.create ( cvt );
		tns.commit ();
		
		assertNotNull ( "Persisted cv-term has a null ID!",cvt.getId () );
		
		CVTerm cvtDB = dao.find ( testName );

		assertNotNull ( "Cannot find persisted cvterm!", cvtDB );
		assertEquals ( "Bad name for retrieved cvterm!", testName, cvtDB.getName () );
	}
}
