/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.xref;

import static junit.framework.Assert.*;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.perfectjpattern.jee.api.integration.dao.ITransaction;

import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.xref.ReferenceSourceDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ReferenceSourceDAOTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private ReferenceSourceDAO<ReferenceSource> srcDao;
	private ReferenceSource src; 
	
	
	@Before
	public void init()
	{
		em = emProvider.getEntityManager ();
		srcDao = new ReferenceSourceDAO<ReferenceSource> ( ReferenceSource.class, em );

		src = new ReferenceSource ( "tests.dao.foo-src-2", "v1.0" );
		src.setDescription ( "The description of FOO-SRC-2" );
		src.setUrl ( "http://tests.dao/foo-src-2/v1.0" );
	}
	
	@After
	public void cleanUpDB ()
	{
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
		ITransaction tns = srcDao.getTransaction ();
		tns.begin ();
		srcDao.create ( src );
		tns.commit ();

		assertTrue ( "Ref Source not stored!", srcDao.contains ( src.getAcc (), src.getVersion () ));
		ReferenceSource srcDB = srcDao.find ( src.getAcc (), src.getVersion () );
		assertEquals ( "Ref Source not stored correctly!", src, srcDB );
		
		assertTrue ( "Accession-only search doesn't work!", srcDao.contains ( src.getAcc () ) );
	}
}
