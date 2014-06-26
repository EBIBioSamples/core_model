package uk.ac.ebi.fg.core_model.toplevel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;

public class AnnotationTest
{
	@Test
	public void testEquivalence ()
	{
		AnnotationType annType1 = new AnnotationType ( "foo type 1" ), annType2 = new AnnotationType ( "foo type 2" );
		
		Annotation ann1 = new TextAnnotation ( annType1, "foo ann 1" ), ann2 = new TextAnnotation ( annType2, "foo ann 2" ), 
							 ann3 = new TextAnnotation ( annType1, "foo ann 1" ), ann4 = new TextAnnotation ( null, "foo ann 1" ), 
							 ann5 = new TextAnnotation ( annType2, null ), ann6 = new TextAnnotation ( null, null ), 
							 ann7 = new TextAnnotation ( annType2, null ), ann8 = new TextAnnotation ( null, "foo ann 1" ), 
							 ann9 = new TextAnnotation ( null, null );
		
		assertFalse ( "ann1.equals ( ann2 ) fails!", ann1.equals ( ann2 ) );
		assertFalse ( "ann2.equals ( ann1 ) fails!", ann2.equals ( ann1 ) );

		assertTrue ( "ann1.equals ( ann3 ) fails!", ann1.equals ( ann3 ) );
		assertTrue ( "ann3.equals ( ann1 ) fails!", ann3.equals ( ann1 ) );

		assertFalse ( "ann1.equals ( ann4 ) fails!", ann1.equals ( ann4 ) );
		assertFalse ( "ann4.equals ( ann1 ) fails!", ann4.equals ( ann1 ) );

		assertTrue ( "ann4.equals ( ann8 ) fails!", ann4.equals ( ann8 ) );
		assertTrue ( "ann8.equals ( ann4 ) fails!", ann8.equals ( ann4 ) );
		
		assertTrue ( "ann5.equals ( ann7 ) fails!", ann5.equals ( ann7 ) );
		assertTrue ( "ann7.equals ( ann5 ) fails!", ann7.equals ( ann5 ) );

		assertFalse ( "ann5.equals ( ann6 ) fails!", ann5.equals ( ann6 ) );
		assertFalse ( "ann6.equals ( ann5 ) fails!", ann6.equals ( ann5 ) );

		assertFalse ( "ann1.equals ( ann6 ) fails!", ann1.equals ( ann6 ) );
		assertFalse ( "ann6.equals ( ann1 ) fails!", ann6.equals ( ann1 ) );

		assertTrue ( "ann6.equals ( ann9 ) fails!", ann6.equals ( ann9 ) );
		assertTrue ( "ann9.equals ( ann6 ) fails!", ann9.equals ( ann6 ) );
		
		Set<Annotation> anns = new HashSet<Annotation> ();
		
		anns.add ( ann1 ); // 1
		anns.add ( ann2 ); // 2
		anns.add ( ann3 ); // 2
		anns.add ( ann4 ); // 3
		anns.add ( ann5 ); // 4
		anns.add ( ann6 ); // 5
		anns.add ( ann7 ); // 5
		anns.add ( ann9 ); // 5
		
		assertTrue ( "anns.contains ( ann1 ) fails!", anns.contains ( ann1 ) );
		assertTrue ( "anns.contains ( ann2 ) fails!", anns.contains ( ann2 ) );
		assertTrue ( "anns.contains ( ann3 ) fails!", anns.contains ( ann3 ) );
		assertTrue ( "anns.contains ( ann4 ) fails!", anns.contains ( ann4 ) );
		assertTrue ( "anns.contains ( ann5 ) fails!", anns.contains ( ann5 ) );
		assertTrue ( "anns.contains ( ann7 ) fails!", anns.contains ( ann7 ) );
		assertTrue ( "anns.contains ( ann8 ) fails!", anns.contains ( ann8 ) );
		assertTrue ( "anns.contains ( ann9 ) fails!", anns.contains ( ann9 ) );
		assertFalse ( "anns.contains ( annType1, 'new ann' ) fails!", anns.contains ( new TextAnnotation ( annType1, "new ann" ) ) );
		
		assertEquals ( "Set size is wrong!", 5, anns.size () );
	}
}
