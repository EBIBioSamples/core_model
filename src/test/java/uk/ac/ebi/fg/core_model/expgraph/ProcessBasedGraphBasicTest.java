package uk.ac.ebi.fg.core_model.expgraph;

import static java.lang.System.out;

import org.junit.Test;

import uk.ac.ebi.fg.core_model.utils.expgraph.ProcessBasedGraphDumper;
import uk.ac.ebi.fg.core_model.utils.test.ProcessBasedTestModel;

/**
 * 
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Jul 12, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ProcessBasedGraphBasicTest
{
	@Test
	public void testCreation()
	{
		ProcessBasedTestModel m = new ProcessBasedTestModel ();
		new ProcessBasedGraphDumper().dump ( out, m.bm1 );
	}
}
