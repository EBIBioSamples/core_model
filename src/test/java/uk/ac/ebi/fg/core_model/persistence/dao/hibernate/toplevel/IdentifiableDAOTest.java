/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.organizational.Contact;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms.CVTermDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.fg.core_model.xref.XRef;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 2, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class IdentifiableDAOTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	private EntityManager em;
	private IdentifiableDAO<XRef> xrefDao;
	private IdentifiableDAO<ReferenceSource> srcDao;
	private IdentifiableDAO<Contact> cntDao;

	private Contact cnt;
	private Annotation ann1, ann2;
	private AnnotationType atype;
	
	/**
	 * Deletes previously created test entities if they're still around.
	 * 
	 */
	@Before
	public void init ()
	{
		em = emProvider.getEntityManager ();
		xrefDao = new IdentifiableDAO<XRef> ( XRef.class, em );
		srcDao = new IdentifiableDAO<ReferenceSource> ( ReferenceSource.class, em );
		cntDao = new IdentifiableDAO<Contact> ( Contact.class, em );

		cnt = new Contact ();
		cnt.setFirstName ( "Mr" ); cnt.setLastName ( "Test" );
		
		atype = new AnnotationType ( "tests.dao.foo-ann-type-1" );

		ann1 = new Annotation ( atype, "foo annotation 1" );
		ann2 = new Annotation ( atype, "foo annotation 2" );
		cnt.addAnnotation ( ann1 );
		cnt.addAnnotation ( ann2 );
	}

	@After
	public void cleanUpDB ()
	{
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		
		XRef xtpl = new XRef ( "tests.dao.foo-xref-1", null );
		for ( XRef xdb: xrefDao.findByExample ( xtpl ) )
			xrefDao.delete ( xdb );
		tns.commit ();

		tns = em.getTransaction ();
		tns.begin ();
		ReferenceSource srcTpl = new ReferenceSource ( "tests.dao.foo-src-1", null );
		for ( ReferenceSource srcDb: srcDao.findByExample ( srcTpl ))
			srcDao.delete ( srcDb );
		tns.commit();
		
		tns = em.getTransaction ();
		tns.begin ();
		
		for ( Contact cntDb: cntDao.findByExample ( cnt, "annotations" ) )
			cntDao.delete ( cntDb );
		tns.commit ();

		assertTrue ( "Test Contact not deleted!", cntDao.findByExample ( cnt ).isEmpty () );
		
		IdentifiableDAO<Annotation> annDao = new IdentifiableDAO<Annotation> ( Annotation.class, em );
		
		/* DEBUG System.out.println ( "\n\n   _____________________ ANNOTATIONS NOT DELETED:" );
		List<Annotation> anns = annDao.findByExample ( ann1 );
		for ( Annotation ann: annDao.findByExample ( ann1 ) )
			System.out.println ( ann );
		for ( Annotation ann: annDao.findByExample ( ann2 ) )
			System.out.println ( ann ); */
		
		assertTrue ( "Test Annotation 1 not deleted!", annDao.findByExample ( ann1 ).isEmpty () );
		assertTrue ( "Test Annotation 2 not deleted!", annDao.findByExample ( ann2 ).isEmpty () );

		CVTermDAO<AnnotationType> annTypeDao = new CVTermDAO<AnnotationType> ( AnnotationType.class, em );
		AnnotationType atypeDB = annTypeDao.find ( atype.getName () );
		if ( atypeDB != null ) 
		{
			tns = em.getTransaction ();
			tns.begin ();
			annTypeDao.delete ( atypeDB );
			tns.commit ();
		}
		assertFalse ( "Test Annotation Type not deleted!", annTypeDao.contains ( atype.getName () ) );		
	}
	
	
	@Test
	public void testBasics () 
	{
		// Create a new Src/XRef, save, fetch.
		ReferenceSource src = new ReferenceSource ( "tests.dao.foo-src-1", "v1.0" );
		src.setDescription ( "The description of FOO-SRC-1" );
		src.setUrl ( "http://tests.dao/foo-src-1/v1.0" );
		
		XRef xref = new XRef ( "tests.dao.foo-xref-1", src );
		
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		
		srcDao.create ( src );
		xrefDao.create ( xref );
		
		tns.commit ();
		
		List<XRef> xrefsDB = xrefDao.findByExample ( xref, "id", "source" );
		assertEquals ( "Stored XREF not found!", 1, xrefsDB.size () );
		
		// TODO: more checks
	}
	
	@Test
	public void testAnnotatable ()
	{
		EntityTransaction tns = em.getTransaction ();

		tns.begin ();
		cntDao.create ( cnt );
		tns.commit ();
		
		assertNotNull ( "Saved contact has null ID!", cnt.getId () );
		assertNotNull ( "Annotation not saved!", ann1.getId () );
		assertNotNull ( "Annotation not saved!", ann2.getId () );
		assertNotNull ( "Annotation Type not saved!", atype.getId () );
	}
}
