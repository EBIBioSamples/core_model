package uk.ac.ebi.fg.core_model.xref;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import uk.ac.ebi.fg.core_model.organizational.Contact;
import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * 
 * A default implementation for the {@link DefaultReferrer} interface. You need to override some ORM for this class
 * to work well in sub-classes, see {@link #references}.
 *
 * <dl><dt>date</dt><dd>Jun 14, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@MappedSuperclass
public abstract class DefaultReferrer extends Identifiable implements Referrer
{
	/**
	 * You need to change the table name with {@link AssociationOverride#joinTable()}. Have a look at {@link Contact}
	 * for an example.
	 */
  @Embedded
	@ElementCollection
	@CollectionTable( name = "referrer_annotation", joinColumns = @JoinColumn( name = "owner_id" ) )
	private Set<XRef> references = new HashSet<XRef> ();
	
	@Override
	public Set<XRef> getReferences ()
	{
		return this.references;
	}

	@Override
	public void setReferences ( Set<XRef> xrefs )
	{
		this.references = xrefs;
	}

	@Override
	public void addReference ( XRef xref )
	{
		this.references.add ( xref );
	}

}
