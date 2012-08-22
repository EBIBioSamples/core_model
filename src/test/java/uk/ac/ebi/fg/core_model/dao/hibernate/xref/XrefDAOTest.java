/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.xref;

import static junit.framework.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.perfectjpattern.jee.api.integration.dao.ITransaction;

import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.fg.core_model.xref.XRef;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class XrefDAOTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private XRefDAO<XRef> xrefDao;
	private ReferenceSourceDAO<ReferenceSource> srcDao;
	private ReferenceSource src;
	private XRef xref;
	
	
	@Before
	public void init()
	{
		em = emProvider.getEntityManager ();
		xrefDao = new XRefDAO<XRef> ( XRef.class, em );
		srcDao = new ReferenceSourceDAO<ReferenceSource> ( ReferenceSource.class, em );

		src = new ReferenceSource ( "tests.dao.foo-src-3", "v1.1" );
		src.setDescription ( "The description of FOO-SRC-3" );
		src.setUrl ( "http://tests.dao/foo-src-3/v1.1" );

		xref = new XRef ( "tests.dao.foo-xref-3", src );
		
		XRef xrefDB = xrefDao.find ( xref.getAcc (), xref.getSource ().getAcc (), xref.getSource ().getVersion () );
		
		if ( xrefDB != null ) 
		{
			ITransaction tns = xrefDao.getTransaction ();
			tns.begin ();
			xrefDao.delete ( xrefDB );
			tns.commit ();
		}
		
		assertFalse ( "Old test x-ref not deleted!", 
			xrefDao.contains ( xref.getAcc (), xref.getSource ().getAcc (), xref.getSource ().getVersion () ) );
		
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
		ITransaction tns = xrefDao.getTransaction ();
		tns.begin ();
		xrefDao.create ( xref );
		tns.commit ();

		assertTrue ( "X-Ref not stored!", xrefDao.contains ( 
			xref.getAcc (), xref.getSource ().getAcc (), xref.getSource ().getVersion () ));
		XRef xrefDB = xrefDao.find ( xref.getAcc (), xref.getSource ().getAcc (), xref.getSource ().getVersion () );
		assertEquals ( "X-Ref not stored correctly!", xref, xrefDB );
		assertTrue ( "Accession-only search doesn't work!", xrefDao.contains ( xref.getAcc (), xref.getSource ().getAcc () ) );
		
		
		assertTrue ( "Ref Source not stored!", srcDao.contains  ( xref.getSource ().getAcc (), xref.getSource ().getVersion () ));
		ReferenceSource srcDB = srcDao.find ( xref.getSource ().getAcc (), xref.getSource ().getVersion () );
		assertEquals ( "Ref Source not stored correctly!", src, srcDB );
	}
}
