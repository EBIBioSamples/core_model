package uk.ac.ebi.fg.core_model.xref;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;

/**
 * A cross reference is something like a publication or a database entry. In terms of the core model, it is an 
 * {@link #acc accession}, referring to the context of a {@link ReferenceSource}. This is not an {@link Accessible}, 
 * despite having an accession property, since the accession for an XRef is unique only within in its context.
 * 
 * We find natural to model this as a type of {@link Annotation}. 
 *
 * <dl><dt>date</dt><dd>Jun 14, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "xref" )
public class XRef extends Annotation
{
	private String acc;

	private ReferenceSource source;

	protected XRef () {
		super ();
	}

	public XRef ( String acc, ReferenceSource source ) {
		super ();
		this.acc = acc;
		this.source = source;
	}

  @Column ( length = Const.COL_LENGTH_S, name = "xref_acc" )
	@Index ( name = "xr_acc" )
	public String getAcc () {
		return this.acc;
	}

	public void setAcc ( String acc ) {
		this.acc = acc;
	}

	@ManyToOne ( 
		targetEntity = ReferenceSource.class, 
		cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } 
	)
	@JoinColumn ( name = "xref_source_id" )
	public ReferenceSource getSource () {
		return this.source;
	}

	protected void setSource ( ReferenceSource source ) {
		this.source = source;
	}

	
  /**
   * If both accessions and reference sources are non-null, compares them, else uses object identity. 
   */
  @Override
  public boolean equals ( Object o ) 
  {
  	if ( o == null ) return false;
  	if ( this == o ) return true;
  	if ( this.getClass () != o.getClass () ) return false;
  	
    // Compare accessions if both are non-null, use identity otherwise
  	XRef that = (XRef) o;
    if ( this.getAcc () == null || that.getAcc () == null ) return false;
    if ( this.getSource () == null || that.getSource () == null ) return false;
    return this.acc.equals ( that.acc ) && this.source.equals ( that.source );
  }
  
  @Override
  public int hashCode() 
  {
  	return this.getAcc () == null || this.getSource () == null 
  		? super.hashCode () 
  		: this.source.hashCode () * 31 + this.acc.hashCode ();
  }  
  
  @Override
	public String toString () 
	{
  	return String.format ( 
  		" %s { id: %d, acc: '%s', source: %s, type: %s, timestamp: %tc, provenance: %s, score: %f, notes: '%s', internalNotes: '%s' }", 
  		this.getClass ().getSimpleName (), this.getId (), this.getAcc (), this.getScore (), this.getType (),
  		this.getTimestamp (), this.getProvenance (), this.getScore (), StringUtils.abbreviate ( this.getNotes (), 20 ), 
  		StringUtils.abbreviate ( this.getInternalNotes (), 20 )
  	);
	}

}
