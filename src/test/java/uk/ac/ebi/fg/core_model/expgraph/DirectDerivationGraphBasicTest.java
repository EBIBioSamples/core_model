package uk.ac.ebi.fg.core_model.expgraph;

import static java.lang.System.out;

import org.junit.Test;

import uk.ac.ebi.fg.core_model.utils.expgraph.DirectDerivationGraphDumper;
import uk.ac.ebi.fg.core_model.utils.test.DirectDerivationTestModel;

/**
 * 
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Jul 12, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DirectDerivationGraphBasicTest
{
	@Test
	public void testCreation()
	{
		DirectDerivationTestModel m = new DirectDerivationTestModel ();
		new DirectDerivationGraphDumper ().dump ( out, m.bm1 );
	}
}
