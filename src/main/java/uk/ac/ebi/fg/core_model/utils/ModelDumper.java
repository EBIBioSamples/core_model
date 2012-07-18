package uk.ac.ebi.fg.core_model.utils;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.fg.core_model.expgraph.Node;

public class ModelDumper
{
	
	private ModelDumper () {
	}

	@SuppressWarnings ( "rawtypes" )
	public static void dump ( PrintStream out, Node<?, ?>... sources )
	{
		Set<Node<?,?>> visited = new HashSet<Node<?,?>> ();
		for ( Node node: sources ) dumpNodeLinks ( out, node, visited );

		visited = new HashSet<Node<?,?>> ();
		for ( Node node: sources ) dumpNodeDetails ( out, node, visited );
	}

	@SuppressWarnings ( "rawtypes" )
	private static void dumpNodeLinks ( PrintStream out, Node<?, ?> node, Set<Node<?,?>> visited )
	{
		if ( visited.contains ( node ) ) return; 
		visited.add ( node );
		
		for ( Node down: node.getDownstreamNodes () ) 
			out.printf ( "%s ( #%d ) -> %s ( #%d )\n", node.getAcc (), node.getId (), down.getAcc (), down.getId () );
		
		for ( Node down: node.getDownstreamNodes () )
			dumpNodeLinks ( out, down, visited );
	}

	
	@SuppressWarnings ( "rawtypes" )
	private static void dumpNodeDetails ( PrintStream out, Node<?, ?> node, Set<Node<?,?>> visited )
	{
		if ( visited.contains ( node ) ) return; 
		visited.add ( node );

		out.println ( node );
		
		for ( Node down: node.getDownstreamNodes () )
			dumpNodeDetails ( out, down, visited );
	}
}
