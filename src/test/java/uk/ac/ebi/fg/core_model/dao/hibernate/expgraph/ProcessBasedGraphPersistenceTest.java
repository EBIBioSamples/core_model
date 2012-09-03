/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.expgraph;

import static java.lang.System.out;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.dao.hibernate.toplevel.AccessibleDAO;
import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;
import uk.ac.ebi.fg.core_model.expgraph.Node;
import uk.ac.ebi.fg.core_model.expgraph.Process;
import uk.ac.ebi.fg.core_model.expgraph.Product;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.utils.expgraph.ProcessBasedGraphDumper;
import uk.ac.ebi.fg.core_model.utils.test.ProcessBasedTestModel;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 23, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class ProcessBasedGraphPersistenceTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private AccessibleDAO<BioMaterial> biomaterialDao;
	private ProcessBasedTestModel model; 

	
	/**
	 * Checks that the {@link Node nodes} in model are loaded/unloaded (depending on checkIsLoaded), issues warnings
	 * and triggers a test failure in case not. 
	 */
	private void verifyTestModel ( Object model, boolean checkIsLoaded ) throws Exception
	{
		AccessibleDAO<Product> productDao = new AccessibleDAO<Product> ( Product.class, em );
		AccessibleDAO<Process> procDao = new AccessibleDAO<Process> ( Process.class, em );
		
		boolean isOK = true;
		
		for ( Field f: this.getClass ().getFields () ) 
		{
			Object o = f.get ( this );
			if ( ! ( o instanceof Node ) ) continue;
			
			Node<Node, Node> node = (Node) o;
			Node<Node, Node> nodeDB = node instanceof Product 
				? productDao.find ( node.getAcc () )
				: procDao.find ( node.getAcc () );
				
			if ( checkIsLoaded )
			{
				if ( nodeDB == null ) {
					out.println ( ">>>> Node '" + node.getAcc () + "' not found in the DB!" );
					isOK = false;
				}
			}
			else
			{
				if ( nodeDB != null ) {
					out.println ( ">>>> Node '" + node.getAcc () + "' still in the DB!" );
					isOK = false;
				}
			}
			assertTrue ( (checkIsLoaded ? "Some test objects not in the DB!": "Some objects still in the DB!" ), isOK );
		}		
	}
	
	@Before
	public void init() throws Exception
	{
		em = emProvider.getEntityManager ();
		biomaterialDao = new AccessibleDAO<BioMaterial> ( BioMaterial.class, em );
		model = new ProcessBasedTestModel ( "tests.dao." );
	}

	
	@After
	public void cleanUpDB () throws Exception
	{
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		model.delete ( em );
		tns.commit ();

		verifyTestModel ( model, false );
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
		ProcessBasedGraphDumper.dump ( out, model.bm1 );

		Node bm1DB = biomaterialDao.findById ( model.bm1.getId () );
		assertNotNull ( "Could not fetch bm1!", bm1DB  );
		
		out.println ( "\n\nReloaded model:" );
		ProcessBasedGraphDumper.dump ( out, model.bm1 );

		verifyTestModel ( model, true );
	}
}
