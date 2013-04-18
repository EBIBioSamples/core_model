package uk.ac.ebi.fg.core_model.xref;

import static java.lang.System.out;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class XRefTest
{
	@Test
	public void testEquivalence ()
	{
		ReferenceSource 
		    src1 = new ReferenceSource ( "_tests:foo_source:1", null ), 
				src2 = new ReferenceSource ( "_tests:foo_source:2", "v1.0" );
		
		XRef ref1 = new XRef ( "foo ref 1", src1 ), ref2 = new XRef ( "foo ref 2", src2 ), 
							 ref3 = new XRef ( "foo ref 1", src1 ), ref4 = new XRef ( "foo ref 1", null ), 
							 ref5 = new XRef ( null, src2 ), ref6 = new XRef ( null, null ), 
							 ref7 = new XRef ( null, src2 ), ref8 = new XRef ( "foo ref 1", null ), 
							 ref9 = new XRef ( null, null );
		
		assertFalse ( "ref1.equals ( ref2 ) fails!", ref1.equals ( ref2 ) );
		assertFalse ( "ref2.equals ( ref1 ) fails!", ref2.equals ( ref1 ) );

		assertTrue ( "ref1.equals ( ref3 ) fails!", ref1.equals ( ref3 ) );
		assertTrue ( "ref3.equals ( ref1 ) fails!", ref3.equals ( ref1 ) );

		assertFalse ( "ref1.equals ( ref4 ) fails!", ref1.equals ( ref4 ) );
		assertFalse ( "ref4.equals ( ref1 ) fails!", ref4.equals ( ref1 ) );

		assertFalse ( "ref4.equals ( ref8 ) fails!", ref4.equals ( ref8 ) );
		assertFalse ( "ref8.equals ( ref4 ) fails!", ref8.equals ( ref4 ) );
		
		assertFalse ( "ref5.equals ( ref7 ) fails!", ref5.equals ( ref7 ) );
		assertFalse ( "ref7.equals ( ref5 ) fails!", ref7.equals ( ref5 ) );

		assertFalse ( "ref5.equals ( ref6 ) fails!", ref5.equals ( ref6 ) );
		assertFalse ( "ref6.equals ( ref5 ) fails!", ref6.equals ( ref5 ) );

		assertFalse ( "ref1.equals ( ref6 ) fails!", ref1.equals ( ref6 ) );
		assertFalse ( "ref6.equals ( ref1 ) fails!", ref6.equals ( ref1 ) );

		assertFalse ( "ref6.equals ( ref9 ) fails!", ref6.equals ( ref9 ) );
		assertFalse ( "ref9.equals ( ref6 ) fails!", ref9.equals ( ref6 ) );
		
		Set<XRef> refs = new HashSet<XRef> ();
		
		refs.add ( ref1 ); // 1
		refs.add ( ref2 ); // 2
		refs.add ( ref3 ); // 2
		refs.add ( ref4 ); // 3
		refs.add ( ref5 ); // 4
		refs.add ( ref6 ); // 5
		refs.add ( ref7 ); // 6
		refs.add ( ref9 ); // 7
		
		out.println ( "Resulting Set:\n  " + refs );
		
		assertTrue ( "refs.contains ( ref1 ) fails!", refs.contains ( ref1 ) );
		assertTrue ( "refs.contains ( ref2 ) fails!", refs.contains ( ref2 ) );
		assertTrue ( "refs.contains ( ref3 ) fails!", refs.contains ( ref3 ) );
		assertTrue ( "refs.contains ( ref4 ) fails!", refs.contains ( ref4 ) );
		assertTrue ( "refs.contains ( ref5 ) fails!", refs.contains ( ref5 ) );
		assertTrue ( "refs.contains ( ref7 ) fails!", refs.contains ( ref7 ) );
		assertTrue ( "refs.contains ( <new existing ref> ) fails!", refs.contains ( new XRef ( "foo ref 1", src1 ) ) );
		assertTrue ( "refs.contains ( ref9 ) fails!", refs.contains ( ref9 ) );
		assertFalse ( "refs.contains ( <new ref> ) fails!", refs.contains ( new XRef ( "new ref", src1 ) ) );
		
		assertEquals ( "Set size is wrong!", 7, refs.size () );
	}
}
