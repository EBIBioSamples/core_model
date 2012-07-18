package uk.ac.ebi.fg.core_model.terms;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;
import uk.ac.ebi.fg.core_model.xref.XRef;

import static java.lang.System.out;

public class OntologyEntryTest
{
	@Test
	public void testEquivalence ()
	{
		ReferenceSource 
		    src1 = new ReferenceSource ( "_tests:foo_source:1", null ), 
				src2 = new ReferenceSource ( "_tests:foo_source:2", "v1.0" );
		
		OntologyEntry oe1 = new OntologyEntry ( "_tests:foo_oe:1", src1 ), oe2 = new OntologyEntry ( "_tests:foo_oe:2", src2 ), 
							 oe3 = new OntologyEntry ( "_tests:foo_oe:1", src1 ), oe4 = new OntologyEntry ( "_tests:foo_oe:1", null ), 
							 oe5 = new OntologyEntry ( null, src2 ), oe6 = new OntologyEntry ( null, null ), 
							 oe7 = new OntologyEntry ( null, src2 ), oe8 = new OntologyEntry ( "_tests:foo_oe:1", null ), 
							 oe9 = new OntologyEntry ( null, null );
		
		assertFalse ( "oe1.equals ( oe2 ) fails!", oe1.equals ( oe2 ) );
		assertFalse ( "oe2.equals ( oe1 ) fails!", oe2.equals ( oe1 ) );

		assertTrue ( "oe1.equals ( oe3 ) fails!", oe1.equals ( oe3 ) );
		assertTrue ( "oe3.equals ( oe1 ) fails!", oe3.equals ( oe1 ) );

		assertFalse ( "oe1.equals ( oe4 ) fails!", oe1.equals ( oe4 ) );
		assertFalse ( "oe4.equals ( oe1 ) fails!", oe4.equals ( oe1 ) );

		assertFalse ( "oe4.equals ( oe8 ) fails!", oe4.equals ( oe8 ) );
		assertFalse ( "oe8.equals ( oe4 ) fails!", oe8.equals ( oe4 ) );
		
		assertFalse ( "oe5.equals ( oe7 ) fails!", oe5.equals ( oe7 ) );
		assertFalse ( "oe7.equals ( oe5 ) fails!", oe7.equals ( oe5 ) );

		assertFalse ( "oe5.equals ( oe6 ) fails!", oe5.equals ( oe6 ) );
		assertFalse ( "oe6.equals ( oe5 ) fails!", oe6.equals ( oe5 ) );

		assertFalse ( "oe1.equals ( oe6 ) fails!", oe1.equals ( oe6 ) );
		assertFalse ( "oe6.equals ( oe1 ) fails!", oe6.equals ( oe1 ) );

		assertFalse ( "oe6.equals ( oe9 ) fails!", oe6.equals ( oe9 ) );
		assertFalse ( "oe9.equals ( oe6 ) fails!", oe9.equals ( oe6 ) );
		
		Set<OntologyEntry> oes = new HashSet<OntologyEntry> ();
		
		oes.add ( oe1 ); // 1
		oes.add ( oe2 ); // 2
		oes.add ( oe3 ); // 2
		oes.add ( oe4 ); // 3
		oes.add ( oe5 ); // 4
		oes.add ( oe6 ); // 5
		oes.add ( oe7 ); // 6
		oes.add ( oe9 ); // 7
		
		out.println ( "Resulting Set:\n  " + oes );
		
		assertTrue ( "oes.contains ( oe1 ) fails!", oes.contains ( oe1 ) );
		assertTrue ( "oes.contains ( oe2 ) fails!", oes.contains ( oe2 ) );
		assertTrue ( "oes.contains ( oe3 ) fails!", oes.contains ( oe3 ) );
		assertTrue ( "oes.contains ( oe4 ) fails!", oes.contains ( oe4 ) );
		assertTrue ( "oes.contains ( oe5 ) fails!", oes.contains ( oe5 ) );
		assertTrue ( "oes.contains ( oe7 ) fails!", oes.contains ( oe7 ) );
		assertTrue ( "oes.contains ( <new existing term> ) fails!", oes.contains ( new OntologyEntry ( "_tests:foo_oe:1", src1 ) ) );
		assertTrue ( "oes.contains ( oe8 ) fails!", oes.contains ( oe9 ) );
		assertFalse ( "oes.contains ( <new term> ) fails!", oes.contains ( new XRef ( "new oe", src1 ) ) );
		
		assertEquals ( "Set size is wrong!", 7, oes.size () );
	}
}
