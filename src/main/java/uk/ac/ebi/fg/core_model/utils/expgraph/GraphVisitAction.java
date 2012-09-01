package uk.ac.ebi.fg.core_model.utils.expgraph;

import uk.ac.ebi.fg.core_model.expgraph.Node;

/** 
 * An action passed to {@link GraphVisitor}. Adapted from AE2 and BII. 
 *
 * <dl><dt>date</dt><dd>Aug 30, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public interface GraphVisitAction 
{
	/**
	 * Executes an action during the visit of an experimental graph. If false is returned, the visit is interrupted and
	 * the method which has started the visit will return false.
	 * 
	 * @param node the experimental pipeline element being visited
	 */
	boolean execute ( Node<?, ?> node );
	
	/** 
	 * This is called by {@link GraphVisitor#reset()} and can be used in case you want to reset the internal status of 
	 * your action (eg: statistics data), without having to create a new one for that, which is relevant for performance. 
	 * 
	 * This is always be called by {@link GraphVisitor} only, so it has package access.
	 */
	public void reset();
}