package uk.ac.ebi.fg.core_model.toplevel;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.terms.CVTerm;

/**
 * TODO: Comment me again! 
 * 
 * Used on a number of of entities to be able to attach them a set of generic and typed properties (annotations). 
 * This class goes in tandem with {@link Annotatable}.
 *
 * <dl><dt>Date</dt><dd>Dec 20, 2007</dd></dl>
 *
 * @author Marco Brandizi (original design/implementation take from AE2)
 * 
 */
@Entity
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE ) // TODO: or maybe JOIN?!
@DiscriminatorColumn ( name = "annotation_class" )
//@DiscriminatorValue ( "generic" ) // TODO: would never be used, not sure Hibernate is fine without any specification
@SequenceGenerator( name = "hibernate_seq", sequenceName = "annotation_seq" )
@Table ( name = "annotation" )
public abstract class Annotation extends Identifiable 
{
	private AnnotationType type;
	private CVTerm provenance;
	private Date timestamp = new Date ();
	private Double score;
	private String notes;
	private String internalNotes;

	
	protected Annotation() {
	}


	@ManyToOne( targetEntity = AnnotationType.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH } )
	@Fetch( FetchMode.JOIN )
	@JoinColumn( name = "type_id", nullable = true )
  public AnnotationType getType() {
    return type;
  }

  protected void setType ( AnnotationType type ) {
     this.type = type;
  }
  
  /**
   * A person or a software component that generated this annotation. 
   */
	@ManyToOne( targetEntity = AnnotationType.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH } )
	@Fetch( FetchMode.JOIN )
	@JoinColumn( name = "prov_id", nullable = true )
  public CVTerm getProvenance ()
	{
		return provenance;
	}

	protected void setProvenance ( CVTerm provenance )
	{
		this.provenance = provenance;
	}

	/**
	 * When this annotation was created or last updated. TODO: Do we need to distinguish between creation/update.
	 */
	@Index ( name = "annot_ts" )
	public Date getTimestamp ()
	{
		return timestamp;
	}

	protected void setTimestamp ( Date timestamp )
	{
		this.timestamp = timestamp;
	}

	
	/**
	 * A measurement of how good or significant this annotation. This can be something like a p-value, or a percentage,
	 * or anything like that. TODO: do we need something like 'scoreType'? For the moment, this can be associated to 
	 * the {@link #getProvenance() provenance} or can be stored into {@link #getInternalNotes() internalNotes}.
	 * 
	 */
	@Index ( name = "annot_score" )
	public Double getScore ()
	{
		return score;
	}

	protected void setScore ( Double score )
	{
		this.score = score;
	}

	/**
	 * Notes that can possibly be shown to the end-user.
	 */
	@Index ( name = "annot_notes" )
	public String getNotes ()
	{
		return notes;
	}

	protected void setNotes ( String notes )
	{
		this.notes = notes;
	}

	/**
	 * Notes that are technical and are not supposed to be understood by the end user, e.g., computational conditions
	 * stored by the tool that computed this annotation.
	 */
	@Column ( name = "internal_notes" )
	@Index ( name = "annot_int_notes" )
	public String getInternalNotes ()
	{
		return internalNotes;
	}

	protected void setInternalNotes ( String internalNotes )
	{
		this.internalNotes = internalNotes;
	}

 
	@Override
  public String toString () 
  {
  	return String.format ( 
  		" %s { id: %d, type: %s, timestamp: %tc, provenance: %s, score: %f, notes: '%s', internalNotes: '%s' }", 
  		this.getClass ().getSimpleName (), this.getId (), this.getType (),
  		this.getTimestamp (), this.getProvenance (), this.getScore (), StringUtils.abbreviate ( this.getNotes (), 20 ), 
  		StringUtils.abbreviate ( this.getInternalNotes (), 20 )
  	);
  }

}
