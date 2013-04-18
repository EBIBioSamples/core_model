/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.expgraph;

import static java.lang.System.out;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;
import uk.ac.ebi.fg.core_model.expgraph.Node;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.AccessibleDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.utils.expgraph.ProcessBasedGraphDumper;
import uk.ac.ebi.fg.core_model.utils.test.DirectDerivationTestModel;
import uk.ac.ebi.fg.core_model.utils.test.ProcessBasedTestModel;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 23, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ( { "rawtypes" } )
public class DirectDerivationGraphPersistenceTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private AccessibleDAO<BioMaterial> biomaterialDao;
	private DirectDerivationTestModel model; 

	
	@Before
	public void init() throws Exception
	{
		em = emProvider.getEntityManager ();
		biomaterialDao = new AccessibleDAO<BioMaterial> ( BioMaterial.class, em );
		model = new DirectDerivationTestModel ( "tests.dao." );
	}

	
	@After
	public void cleanUpDB () throws Exception
	{
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		model.delete ( em );
		tns.commit ();

		ProcessBasedTestModel.verifyTestModel ( em, model, false );
	}
	
	@Test
	public void testBasics () throws Exception
	{
		// Save
		// 
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		biomaterialDao.create ( model.bm1 );
		biomaterialDao.getOrCreate ( model.bm2 );
		tns.commit ();

		out.println ( "Saved model:" );
		new ProcessBasedGraphDumper().dump ( out, model.bm1 );

		Node bm1DB = biomaterialDao.find ( model.bm1.getId () );
		assertNotNull ( "Could not fetch bm1!", bm1DB  );
		
		out.println ( "\n\nReloaded model:" );
		new ProcessBasedGraphDumper ().dump ( out, model.bm1 );

		ProcessBasedTestModel.verifyTestModel ( em, model, true );
	}
}
