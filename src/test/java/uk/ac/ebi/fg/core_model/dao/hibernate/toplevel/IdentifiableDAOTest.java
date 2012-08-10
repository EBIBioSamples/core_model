/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.toplevel;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.perfectjpattern.jee.api.integration.dao.ITransaction;

import uk.ac.ebi.fg.core_model.organizational.Contact;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.fg.core_model.xref.XRef;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

import static junit.framework.Assert.*;

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

		ITransaction tns = xrefDao.getTransaction ();
		tns.begin ();
		
		XRef xtpl = new XRef ( "tests.dao.foo-xref-1", null );
		for ( XRef xdb: xrefDao.findByExample ( xtpl ) )
			xrefDao.delete ( xdb );
		
		ReferenceSource srcTpl = new ReferenceSource ( "tests.dao.foo-src-1", null );
		for ( ReferenceSource srcDb: srcDao.findByExample ( srcTpl ))
			srcDao.delete ( srcDb );
		
		Contact cntTpl = new Contact ();
		cntTpl.setFirstName ( "Mr" ); cntTpl.setLastName ( "Test" );
		for ( Contact cntDb: cntDao.findByExample ( cntTpl ) )
			cntDao.delete ( cntDb );
		
		tns.commit ();
		
		// TODO: check they're gone
	}
	
	@Test
	public void basicCreationTest () 
	{
		// Create a new Src/XRef, save, fetch.
		ReferenceSource src = new ReferenceSource ( "tests.dao.foo-src-1", "v1.0" );
		src.setDescription ( "The description of FOO-SRC-1" );
		src.setUrl ( "http://tests.dao/foo-src-1/v1.0" );
		
		XRef xref = new XRef ( "tests.dao.foo-xref-1", src );
		
		ITransaction tns = xrefDao.getTransaction ();
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
		Contact cnt = new Contact ();
		cnt.setFirstName ( "Mr" ); cnt.setLastName ( "Test" );
		
		AnnotationType atype = new AnnotationType ( "tests.dao.foo-ann-type-1" );
		Annotation ann1 = new Annotation ( atype, "foo annotation 1" );
		Annotation ann2 = new Annotation ( atype, "foo annotation 2" );
		
		cnt.addAnnotation ( ann1 );
		cnt.addAnnotation ( ann2 );
		
		ITransaction tns = cntDao.getTransaction ();

		tns.begin ();
		cntDao.create ( cnt );
		tns.commit ();
		
		// TODO : checks
	}
}
