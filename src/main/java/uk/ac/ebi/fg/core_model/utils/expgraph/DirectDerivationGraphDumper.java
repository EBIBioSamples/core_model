package uk.ac.ebi.fg.core_model.utils.expgraph;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import uk.ac.ebi.fg.core_model.expgraph.Node;
import uk.ac.ebi.fg.core_model.expgraph.Product;

/**
 * <p>Dumps the contents of an experimental graph. Useful for testing/debugging purposes.</p>
 * 
 * <p>This dumper considers the direct-derivation relations, {@link Product#getDerivedFrom() derived-from} and 
 * {@link Product#getDerivedInto() derived-into}, while {@link ProcessBasedGraphVisitor} uses upstream/downstream links.</p>
 * 
 * <dl><dt>date</dt><dd>Aug 30, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class DirectDerivationGraphDumper
{
	public DirectDerivationGraphDumper () {
	}

	public <P extends Product<?>> void dump ( final PrintStream out, P... sources )
	{
		dump ( out, Arrays.asList ( sources ) );
	}

	public <P extends Product<?>> void dump ( final PrintStream out, Collection<P> sources )
	{
		// Graph
		//
		DirectDerivationGraphVisitor visitor = new DirectDerivationGraphVisitor ( new DefaultAbstractGraphVisitAction()
		{
			@Override
			public boolean execute ( Node node )
			{
				for ( Node down: (Set<Node>) node.getDownstreamNodes () ) 
					out.printf ( "%s ( #%d ) -> %s ( #%d )\n", node.getAcc (), node.getId (), down.getAcc (), down.getId () );
				return true;
			}
		});
		for ( Product<?> node: sources ) visitor.visit ( node );

		// Attributes
		//
		visitor = new DirectDerivationGraphVisitor ( new DefaultAbstractGraphVisitAction()
		{
			@Override
			public boolean execute ( Node node )
			{
				dumpProduct ( out, (Product<?>) node );
				return true;
			}
		});
		for ( Product<?> node: sources ) visitor.visit ( node );
	}
	
	/** 
	 * Prints node.toString() by default. Customise this method if you want something else.
	 */
	public void dumpProduct ( PrintStream out, Product<?> node )
	{
		out.println ( node );
	}
}
