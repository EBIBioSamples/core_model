package uk.ac.ebi.fg.core_model.toplevel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static junit.framework.Assert.*; 

import uk.ac.ebi.fg.core_model.dao.hibernate.toplevel.MyAccessible;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;

public class AccessibleTest
{
	
	@Test
	public void testEquivalence ()
	{
		Accessible acc1 = new MyAccessible ( "FOO-1" ), acc2 = new MyAccessible ( "FOO-2" ), 
							 acc3 = new MyAccessible ( "FOO-1" ), acc4 = new MyAccessible ( null ), acc5 = new MyAccessible ( null );
		
		assertFalse ( "acc1.equals ( acc2 ) fails!", acc1.equals ( acc2 ) );
		assertFalse ( "acc2.equals ( acc1 ) fails!", acc2.equals ( acc1 ) );

		assertTrue ( "acc1.equals ( acc3 ) fails!", acc1.equals ( acc3 ) );
		assertTrue ( "acc3.equals ( acc1 ) fails!", acc3.equals ( acc1 ) );

		assertFalse ( "acc1.equals ( acc4 ) fails!", acc1.equals ( acc4 ) );
		assertFalse ( "acc4.equals ( acc1 ) fails!", acc4.equals ( acc1 ) );

		assertFalse ( "acc4.equals ( acc5 ) fails!", acc4.equals ( acc5 ) );
		assertFalse ( "acc5.equals ( acc4 ) fails!", acc5.equals ( acc4 ) );
		
		Accessible acc6 = new MyAccessible ( "FOO-2" );
		
		Set<Accessible> accs = new HashSet<Accessible> ();
		accs.addAll ( Arrays.asList ( new Accessible [] { acc1, acc2, acc3, acc4 } ) );
		
		assertTrue ( "accs.contains ( acc1 ) fails!", accs.contains ( acc1 ) );
		assertTrue ( "accs.contains ( acc2 ) fails!", accs.contains ( acc2 ) );
		assertTrue ( "accs.contains ( acc4 ) fails!", accs.contains ( acc4 ) );

		assertFalse ( "accs.contains ( acc5 ) fails!", accs.contains ( acc5 ) );
		assertTrue ( "accs.contains ( acc6 ) fails!", accs.contains ( acc6 ) );
		
		assertEquals ( "Set size is wrong!", 3, accs.size () );

	}
}
