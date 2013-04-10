/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.ebi.fg.core_model.organizational.Contact;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms.CVTermDAO;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.IdentifiableDAO;
import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.toplevel.Annotatable;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

import static junit.framework.Assert.*;

/**
 * <p>Show how you can create multiple tables for {@link Annotation}. This class is normally mapped into one table and 
 * referred by {@link Annotatable}s by means of join table. The code in this test shows how it can be split into multiple
 * tables, one per annotatable.</p>
 * 
 * <p>Note that we've tried to realise this by using {@link ElementCollection} and {@link Embeddable}, but the Hibernate 
 * implementation of these annotations are horrible: it doesn't provide an easy mechanism to assign an auto-generated 
 * ID to Annotation and use it as primary key. It ends up using either all fields (not good to have 
 * {@link Annotation#getText() Annotation.text} as part of the primary key), or an insufficient set of fields (text + the
 * annotatable reference, which prevents one from adding two annotations of the same type to the same annotatable).</p>
 * 
 * <p>The way shown here implies writing extra code that is worth nothing but making Hibernate to do what you want. I 
 * know, it sucks, but that's it. :-(</p>
 * 
 *
 * <dl><dt>date</dt><dd>Aug 3, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class RemappedAnnotationTest
{
	/**
	 * An example of {@link Annotatable} that maps its {@link Annotation}s into a specific table. 
	 *
	 * <dl><dt>date</dt><dd>Aug 3, 2012</dd></dl>
	 * @author Marco Brandizi
	 *
	 */
	@Entity
	@Table ( name = "test_contact" )
	public static class MyContact extends Contact
	{
		/**
		 * You need an extension without any real semantic additions in order to have a specific table for my contact's 
		 * annotations. 
		 *
		 * <dl><dt>date</dt><dd>Aug 3, 2012</dd></dl>
		 * @author Marco Brandizi
		 *
		 */
		@Entity
		@Table ( name = "test_contact_ann" )
		public static class MyContactAnnotation extends Annotation
		{
			protected MyContactAnnotation () {
				super ();
			}

			public MyContactAnnotation ( AnnotationType type, String text ) {
				super ( type, text );
			}
			
			/**
			 * Used to make necessary conversions (see below).
			 */
			public MyContactAnnotation ( Annotation base )
			{
				this.setId ( base.getId () );
				this.setText ( base.getText () );
				this.setType ( base.getType () );
			}
		}
		

	  public MyContact () {
			super ();
		}
	  
	  /**
	   * Re-defines the ORM so that now the target is {@link MyContactAnnotation} and hence the specific table where
	   * this annotations are stored. Moreover, it won't use a join table any more, it will use a foreign key straight in 
	   * the {@link MyContactAnnotation}'s table instead.
	   *  
	   */
		@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MyContactAnnotation.class )
	  @JoinColumn ( name = "owner_id" )
		@Override
		public Set<Annotation> getAnnotations () {
			return super.getAnnotations ();
		}
		
		/**
		 * Scans all the elements in the parameter and passes them to {@link #addAnnotation(Annotation)}.
		 */
	  @Override
		public void setAnnotations ( Set<Annotation> annotations )
		{
	  	super.setAnnotations ( new HashSet<Annotation> () );
			for ( Annotation ann: annotations )
				this.addAnnotation ( ann );
				
		}


	  /**
	   * <p>This adapts the element to the need to store {@link MyContactAnnotation}, if the parameter is already an instance
	   * of this type, it calls {@link #addAnnotation(MyContactAnnotation)}, else it creates a corresponding 
	   * {@link MyContactAnnotation}, using {@link MyContactAnnotation#MyContactAnnotation(Annotation)}, and then adds
	   * this new (semantically equivalent) annotation, rather than the parameter.</p>
	   * 
	   * <p><b>Please Note</b>: the description above should make clear that you cannot assume that exactly the same 
	   * object passed as input to this method is stored, just that the possibly new annotation will have the same contents
	   * (and that the parameter and the new annotation will be equivalent according to the usual equals() and hashCode()).</p> 
	   * 
	   */
		@Override
		public void addAnnotation ( Annotation annotation )
		{
			if ( ( annotation instanceof MyContactAnnotation ) )
				addAnnotation ( (MyContactAnnotation) annotation );
			else
				addAnnotation ( new MyContactAnnotation ( annotation ) );
		}
	  
		/**
		 * Just replaces the old addAnnotation. @see {@link #addAnnotation(Annotation)}.
		 */
		public void addAnnotation ( MyContactAnnotation annotation )
		{
			super.addAnnotation ( annotation );
		}
		
	}

	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );
	private EntityManager em;
	
	private IdentifiableDAO<MyContact> cntDao;
	private MyContact cnt;
	private AnnotationType atype;
	private Annotation ann1, ann2;
	
	/**
	 * Deletes previously-created stuff, if it's still there. 
	 */
	@Before
	public void init ()
	{
		em = emProvider.getEntityManager ();
		cntDao = new IdentifiableDAO<MyContact> ( MyContact.class, em );

		cnt = new MyContact ();
		cnt.setFirstName ( "Mr Specific" ); cnt.setLastName ( "Contact Test" );
		atype = new AnnotationType ( "tests.mycontact.foo-ann-type-1" );
		ann1 = new Annotation ( atype, "foo specific annotation 1" );
		ann2 = new Annotation ( atype, "foo specific annotation 2" );
		cnt.addAnnotation ( ann1 );
		cnt.addAnnotation ( ann2 );
	}
	
	@After
	public void cleanUpDB ()
	{
		MyContact cntTpl = new MyContact ();
		cnt.setFirstName ( "Mr Specific" ); cnt.setLastName ( "Contact Test" );

		// You need to first retrieve the objets and their attached annotations and then go ahead with the transaction, 
		// if you find and delete within the same transaction, you'll get 'A collection with cascade=”all-delete-orphan” 
		// was no longer referenced by the owning entity instance'
		// 
		List<MyContact> cnts = cntDao.findByExample ( cntTpl );
		EntityTransaction tns = em.getTransaction ();
		tns.begin ();
		for ( MyContact cntDb: cnts )
			cntDao.delete ( cntDb );
		tns.commit ();
		
		IdentifiableDAO<Annotation> annDao = new IdentifiableDAO<Annotation> ( Annotation.class, em );
		
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
	public void testMyContact () 
	{
		EntityTransaction tns = em.getTransaction ();
		
		tns.begin ();
		cntDao.create ( cnt );
		tns.commit ();
		
		cnt = cntDao.findById ( cnt.getId () );
		assertNotNull ( "MyContact not saved!", cnt );
		assertEquals ( "My Annotations not saved!", 2, cnt.getAnnotations ().size () );
	}
}
