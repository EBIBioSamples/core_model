package uk.ac.ebi.fg.core_model.xref;

import java.util.Set;

/**
 * 
 * The referrer interface, i.e., something able to bring {@link XRef cross-references} with it. 
 *
 * <dl><dt>date</dt><dd>Jun 14, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public interface Referrer
{
	public Set<XRef> getReferences ();
	public void addReference ( XRef xref );
	public void setReferences ( Set<XRef> xrefs );
}
