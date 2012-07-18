package uk.ac.ebi.fg.core_model.terms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static junit.framework.Assert.*; 

import uk.ac.ebi.fg.core_model.terms.CVTerm;

public class CVTermTest
{
	private static class MyCVTerm extends CVTerm
	{
		public MyCVTerm ( String name ) {
			super ( name );
		}
		
	}
	
	@Test
	public void testEquivalence ()
	{
		CVTerm cvt1 = new MyCVTerm ( "term 1" ), cvt2 = new MyCVTerm ( "term 2" ), 
							 cvt3 = new MyCVTerm ( "term 1" ), cvt4 = new MyCVTerm ( null ), cvt5 = new MyCVTerm ( null );
		
		assertFalse ( "cvt1.equals ( cvt2 ) fails!", cvt1.equals ( cvt2 ) );
		assertFalse ( "cvt2.equals ( cvt1 ) fails!", cvt2.equals ( cvt1 ) );

		assertTrue ( "cvt1.equals ( cvt3 ) fails!", cvt1.equals ( cvt3 ) );
		assertTrue ( "cvt3.equals ( cvt1 ) fails!", cvt3.equals ( cvt1 ) );

		assertFalse ( "cvt1.equals ( cvt4 ) fails!", cvt1.equals ( cvt4 ) );
		assertFalse ( "cvt4.equals ( cvt1 ) fails!", cvt4.equals ( cvt1 ) );

		assertFalse ( "cvt4.equals ( cvt5 ) fails!", cvt4.equals ( cvt5 ) );
		assertFalse ( "cvt5.equals ( cvt4 ) fails!", cvt5.equals ( cvt4 ) );
		
		CVTerm cvt6 = new MyCVTerm ( "term 2" );
		
		Set<CVTerm> cvts = new HashSet<CVTerm> ();
		cvts.addAll ( Arrays.asList ( new CVTerm [] { cvt1, cvt2, cvt3, cvt4 } ) );
		
		assertTrue ( "cvts.contains ( cvt1 ) fails!", cvts.contains ( cvt1 ) );
		assertTrue ( "cvts.contains ( cvt2 ) fails!", cvts.contains ( cvt2 ) );
		assertTrue ( "cvts.contains ( cvt4 ) fails!", cvts.contains ( cvt4 ) );

		assertFalse ( "cvts.contains ( cvt5 ) fails!", cvts.contains ( cvt5 ) );
		assertTrue ( "cvts.contains ( cvt6 ) fails!", cvts.contains ( cvt6 ) );
		
		assertEquals ( "Set size is wrong!", 3, cvts.size () );

	}
}
