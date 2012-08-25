/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.expgraph;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.perfectjpattern.jee.api.integration.dao.ITransaction;

import uk.ac.ebi.fg.core_model.dao.hibernate.toplevel.AccessibleDAO;
import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.utils.test.ProcessBasedTestModel;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 23, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ExpGraphPersistenceTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private AccessibleDAO<BioMaterial> biomaterialDao;

	@Before
	public void init() 
	{
		em = emProvider.getEntityManager ();
		biomaterialDao = new AccessibleDAO<BioMaterial> ( BioMaterial.class, em );
		
		// TODO: delete the test graph and check it has gone
	}
	
	@Test
	public void testBasics ()
	{
		ProcessBasedTestModel model = new ProcessBasedTestModel ( "tests.dao." );
		ITransaction tns = biomaterialDao.getTransaction ();
		tns.begin ();
		biomaterialDao.create ( model.bm1 );
		//biomaterialDao.getOrCreate ( model.bm2 );
		tns.commit ();
	}
}
