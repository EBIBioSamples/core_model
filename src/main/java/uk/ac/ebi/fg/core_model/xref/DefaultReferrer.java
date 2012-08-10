package uk.ac.ebi.fg.core_model.xref;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * 
 * A default implementation for the {@link DefaultReferrer} interface.
 *
 * <dl><dt>date</dt><dd>Jun 14, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@MappedSuperclass
public abstract class DefaultReferrer extends Identifiable implements Referrer
{
	private Set<XRef> references = new HashSet<XRef> ();
	
  @OneToMany( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true )
  @JoinTable ( joinColumns = @JoinColumn ( name = "owner_id" ), inverseJoinColumns = @JoinColumn ( name = "xref_id" ) )
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
