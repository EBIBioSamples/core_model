package uk.ac.ebi.fg.core_model.expgraph;

import org.junit.Test;

import uk.ac.ebi.fg.core_model.utils.ModelDumper;
import uk.ac.ebi.fg.core_model.utils.test.ProcessBasedTestModel;

import static java.lang.System.out;

/**
 * 
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Jul 12, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ExpGraphBasicTest
{
	@Test
	public void testCreation()
	{
		ProcessBasedTestModel m = new ProcessBasedTestModel ();
		ModelDumper.dump ( out, m.bm1, m.bm2 );
	}
}
