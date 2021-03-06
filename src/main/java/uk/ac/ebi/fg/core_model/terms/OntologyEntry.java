package uk.ac.ebi.fg.core_model.terms;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;
import uk.ac.ebi.fg.core_model.toplevel.Annotatable;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;


/**
 * A reference ontology term, which can be identified by an accession and a reference to the ontology source.
 * Note that accessions are not unique within any OE table, since they're assigned by 3rd parties and are unique only
 * together with the term's {@link ReferenceSource} (ie, only in their own ontology). For this same reason, this 
 * class doesn't extend {@link Accessible}. 
 * <p/>
 *
 * 
 * @author Nataliya Sklyar
 * @author mdylag
 * @author Tony Burdett
 * @author Marco Brandizi
 * 
 * @date Jul 12, 2007, imported from the AE2 model in 2012
 */
@Entity
@Table( name = "onto_entry"
	/* TODO:We cannot add this as yet, cause the current databases have multiple entires for these fields.
	uniqueConstraints = @UniqueConstraint ( columnNames = { "acc", "source_id" }, name = "oe_acc_src_idx" ) */
)
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn ( name = "term_category" )
@DiscriminatorValue ( "generic" )
public class OntologyEntry extends Annotatable
{
	private String acc, label; 
	
	private ReferenceSource source;
	
	protected OntologyEntry ()
	{
		super ();
	}

	public OntologyEntry ( String acc, ReferenceSource source )
	{
		super ();
		this.acc = acc;
		this.source = source;
	}

  @Column ( length = Const.COL_LENGTH_L, nullable = false ) // We need it long, cause it can contains URIs
	public String getAcc () {
		return acc;
	}

	protected void setAcc ( String acc ) {
		this.acc = acc;
	}

  @Column ( length = Const.COL_LENGTH_M )
  @Index ( name = "oe_label" )
	public String getLabel () {
		return label;
	}

	public void setLabel ( String label ) {
		this.label = label;
	}
	
	@ManyToOne ( 
		targetEntity = ReferenceSource.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH },
		fetch = FetchType.LAZY
	)
	@JoinColumn( name = "source_id" )
	public ReferenceSource getSource ()
	{
		return source;
	}

	protected void setSource ( ReferenceSource source )
	{
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
  	OntologyEntry that = (OntologyEntry) o;
    if ( this.getAcc () == null | that.getAcc () == null ) return false;
    if ( this.getSource () == null ? that.getSource () != null : !this.source.equals ( that.getSource () ) ) return false;
    return this.acc.equals ( that.acc );
  }
  
  @Override
  public int hashCode() 
  {
  	if ( this.getAcc () == null ) return super.hashCode ();
  	
  	int result = this.getSource () == null ? 0 : this.source.hashCode (); 
  	result = result * 31 + this.acc.hashCode ();
  	return result;
  }

  @Override
  public String toString() 
  {
		return String.format ( 
			"%s { id: %d, acc: '%s', source: %s }" , 
			this.getClass ().getSimpleName (), getId (), getAcc (), 
			getSource () == null 
			  ? null 
			  : String.format ( "{ id: %d, '%s', ver: '%s' }", source.getId (), source.getAcc (), source.getVersion () ) 
		);
  }
	
}
