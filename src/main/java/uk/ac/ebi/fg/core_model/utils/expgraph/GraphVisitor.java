package uk.ac.ebi.fg.core_model.utils.expgraph;


import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.fg.core_model.expgraph.Node;


/**
 * <p>Allows to visit an experimental graph starting from a node and toward downstream or upstream direction.
 * Visits are arranged with the command pattern, so that an action (command) is invoked at each visit.
 * The class keeps track of the visited nodes, no node is visited twice. You have a method to reset the 
 * visited node memory (without need to recreate another instance), so that you can decide when the graph
 * or parts of it have to be revisited.</p>
 * 
 * <p>This was adapted from AE2 and the <a href = "http://isatab.sourceforge.net">ISA Project</a>.</p>
 *
 * <dl><dt>date</dt><dd>Aug 30, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class GraphVisitor
{
	private Set<Node> visitedNodes = new HashSet<Node> ();
	private GraphVisitAction action;
	
	public GraphVisitor ( GraphVisitAction action ) {
		this.action = action;
	}
	
	public GraphVisitor () {
	}
	
	/** 
	  * walks through the downstream and upstream graphs which depart and arrive at the parameter, execute the action parameter.
	  * Returns false and interrupt the visit if at some point the visit() method in the action returns false.
	  * 
	  * <p><b>PLEASE NOTE</b>: This is not the same as calling {@link #visitBackward(Node)} + {@link #visitForward(Node)}, 
	  * because here all the graph that is reachable from the initial node is traversed, i.e.: all the inputs and outputs
	  * of a processing are visited, while the outputs(inputs) only are visited by the forward(backward) visit.
	  *  
	  */
	public boolean visit ( Node<?, ?> node ) 
	{
		if ( visitedNodes.contains ( node ) ) return true;
		visitedNodes.add ( node );
		
		if ( action == null ) throw new IllegalStateException ( 
			"Internal error: cannot visit an experimental graph without a visitor" 
		);
		if ( !action.execute ( node ) ) return false;
		
		for ( Node up: (Set<Node>) node.getUpstreamNodes () ) 
			if ( !visit ( up ) ) return false;
		for ( Node down: (Set<Node>) node.getDownstreamNodes () ) 
			if ( !visit ( down ) ) return false;
		return true;
	}
	

	/**
	 * Walks through the graph that departs from the parameter and spans to its downstream nodes. Executes the action in 
	 * the {@link #getAction() visitor} for each visited node.
	 * 
	 * Each node is visited only once, unless {@link #reset()} is called. 
	 * 
	 * Returns false and interrupts the visit if at some point the visit() method in the action returns false.
	 * 
	 * @param skipFirst when true, the current node is not passed to the visiting action, we only go forward with 
	 * its downstream nodes. This is useful to call a backward visit after a forward visit (which is different than
	 * {@link #visit(Node)}).
	 * 
	 * @see #visit(Node)
	 *  
	 */
	public boolean visitForward ( Node<?, ?> node, boolean skipFirst ) 
	{
		if ( !skipFirst )
		{
			if ( visitedNodes.contains ( node ) ) return true;
			visitedNodes.add ( node );
			
			if ( action == null ) throw new IllegalStateException ( 
				"Internal error: cannot visit an experimental graph without a visitor" 
			);
			
			if ( !action.execute ( node ) ) return false;
		}

		for ( Node down: (Set<Node>) node.getDownstreamNodes () ) 
			if ( !visitForward ( down ) ) return false;
		
		return true;
	}

	/**
	 * A wrapper with skipFirst = false
	 * 
	 */
	public boolean visitForward ( Node<?, ?> node ) {
		return visitForward ( node, false );
	} 
	

	/**
	 * Walks back the graph that reaches the parameter node through its upstream nodes. Executes the action in 
	 * the {@link #getAction() visitor} for each visited node.
	 * 
	 * Each node is visited only once, unless {@link #reset()} is called. 
	 * 
	 * Returns false and interrupts the visit if at some point the visit() method in the action returns false.
	 * 
	 * @param skipFirst when true, the current node is not passed to the visiting action, we only go forward with 
	 * its downstream nodes. This is useful to call a backward visit after a forward visit (which is different than
	 * {@link #visit(Node)}).
	 * 
	 * @see #visit(Node)
	 *  
	 */
	public boolean visitBackward ( Node<?, ?> node, boolean skipFirst ) 
	{
		if ( !skipFirst )
		{
			if ( visitedNodes.contains ( node ) ) return true;
			visitedNodes.add ( node );
			
			if ( action == null ) throw new IllegalStateException ( 
				"Internal error: cannot visit an experimental graph without a visitor" 
			);
			if ( !action.execute ( node ) ) return false;
		}
		
		for ( Node up: (Set<Node>) node.getUpstreamNodes () ) 
			if ( !visitBackward ( up ) ) return false;
		return true;
	}

	/**
	 * A wrapper with skipFirst = false
	 * 
	 */
	public boolean visitBackward ( Node<?, ?> node ) {
		return visitBackward ( node, false );
	} 

	
	
	/**
	 * Tells if a certain node has been visited by this instance of the class. Will return always false soon after the
	 * invocation of {@link #reset()}. 
	 * 
	 */
	public boolean isVisited ( Node<?, ?> node ) {
		return this.visitedNodes.contains ( node );
	}

	/**
	 * @return The action invoked at each visit.
	 * 
	 */
	public GraphVisitAction getAction () {
		return action;
	}

	/**
	 * The action invoked at each visit.
	 */
	public void setAction ( GraphVisitAction action ) {
		this.action = action;
	}
	
	/**
	 * Set a new action and call {@link #reset()}, i.e.: mark the pipeline as all unvisited. 
	 * 
	 */
	public void setActionAndReset ( GraphVisitAction action ) {
		this.action = action;
		this.reset ();
	}
	
	
	/**
	 * Un-marks all the nodes that were marked as visited during visit method calls. This allows to restart a 
	 * graph walk, without the need to create a new instance of this class.
	 * 
	 * Additionally this method call {@link #getAction()}.{@link GraphVisitAction#reset() reset()}.
	 * 
	 */
	public void reset () 
	{
		visitedNodes.clear ();
		if ( this.action != null ) this.action.reset ();
	}
	
}
