/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.terms;

import static junit.framework.Assert.*;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.perfectjpattern.jee.api.integration.dao.ITransaction;

import uk.ac.ebi.fg.core_model.dao.hibernate.xref.ReferenceSourceDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OntologyEntryDAOTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private OntologyEntryDAO<OntologyEntry> oeDao;
	private ReferenceSourceDAO<ReferenceSource> srcDao;
	private ReferenceSource src;
	private OntologyEntry oe;
	
	
	@Before
	public void init()
	{
		em = emProvider.getEntityManager ();
		oeDao = new OntologyEntryDAO<OntologyEntry> ( OntologyEntry.class, em );
		srcDao = new ReferenceSourceDAO<ReferenceSource> ( ReferenceSource.class, em );

		src = new ReferenceSource ( "tests.dao.foo-src-4", "v2.0" );
		src.setDescription ( "The description of FOO-SRC-4" );
		src.setUrl ( "http://tests.dao/foo-src-4/v2.0" );

		oe = new OntologyEntry ( "tests.dao.foo-oe-4", src );
		oe.setLabel ( "Test OE 4" );
	}

	
	@After
	public void cleanUpDB ()
	{
		OntologyEntry oeDB = oeDao.find ( oe.getAcc (), oe.getSource ().getAcc (), oe.getSource ().getVersion () );
		
		if ( oeDB != null ) 
		{
			ITransaction tns = oeDao.getTransaction ();
			tns.begin ();
			oeDao.delete ( oeDB );
			tns.commit ();
		}
		
		assertFalse ( "Old test OE not deleted!", 
			oeDao.contains ( oe.getAcc (), oe.getSource ().getAcc (), oe.getSource ().getVersion () ) );
		
		ReferenceSource srcDB = srcDao.find ( src.getAcc (), src.getVersion () );
		if ( srcDB != null ) 
		{
			ITransaction tns = srcDao.getTransaction ();
			tns.begin ();
			srcDao.delete ( srcDB );
			tns.commit ();
		}
		
		assertFalse ( "Old test source not deleted!", srcDao.contains ( src.getAcc (), src.getVersion () ) );		
	}
	
	
	@Test
	public void testBasics ()
	{
		ITransaction tns = oeDao.getTransaction ();
		tns.begin ();
		oeDao.create ( oe );
		tns.commit ();

		assertTrue ( "OE not stored!", oeDao.contains ( 
			oe.getAcc (), oe.getSource ().getAcc (), oe.getSource ().getVersion () ));
		OntologyEntry oeDB = oeDao.find ( oe.getAcc (), oe.getSource ().getAcc (), oe.getSource ().getVersion () );
		assertEquals ( "OE not stored correctly!", oe, oeDB );
		assertTrue ( "Accession-only search doesn't work!", oeDao.contains ( oe.getAcc (), oe.getSource ().getAcc () ) );
		
		
		assertTrue ( "Ref Source not stored!", srcDao.contains  ( oe.getSource ().getAcc (), oe.getSource ().getVersion () ));
		ReferenceSource srcDB = srcDao.find ( oe.getSource ().getAcc (), oe.getSource ().getVersion () );
		assertEquals ( "Ref Source not stored correctly!", src, srcDB );
	}
}
