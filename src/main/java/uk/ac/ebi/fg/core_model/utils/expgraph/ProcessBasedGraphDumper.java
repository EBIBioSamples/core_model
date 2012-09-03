package uk.ac.ebi.fg.core_model.utils.expgraph;

import java.io.PrintStream;
import java.util.Set;

import uk.ac.ebi.fg.core_model.expgraph.Node;

/**
 * <p>Dumps the contents of an experimental graph. Useful for testing/debugging purposes.</p>
 * 
 * <p>This dumper considers {@link Node#getUpstreamNodes() upstream}/{@link Node#getDownstreamNodes() downstream} 
 * relations, while {@link DirectDerivationGraphDumper} works with direct-derivation relations.</p>.
 * 
 * 
 * <dl><dt>date</dt><dd>Aug 30, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ProcessBasedGraphDumper
{
	private ProcessBasedGraphDumper () {
	}

	public static void dump ( final PrintStream out, Node<?, ?>... sources )
	{
		// Graph
		//
		ProcessBasedGraphVisitor visitor = new ProcessBasedGraphVisitor ( new DefaultAbstractGraphVisitAction()
		{
			@Override
			public boolean execute ( Node node )
			{
				for ( Node down: (Set<Node>) node.getDownstreamNodes () ) 
					out.printf ( "%s ( #%d ) -> %s ( #%d )\n", node.getAcc (), node.getId (), down.getAcc (), down.getId () );
				return true;
			}
		});
		for ( Node node: sources ) visitor.visit ( node );

		// Attributes
		//
		visitor = new ProcessBasedGraphVisitor ( new DefaultAbstractGraphVisitAction()
		{
			@Override
			public boolean execute ( Node node )
			{
				out.println ( node );
				return true;
			}
		});
		for ( Node node: sources ) visitor.visit ( node );
	}
}
