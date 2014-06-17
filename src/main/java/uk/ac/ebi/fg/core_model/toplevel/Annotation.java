package uk.ac.ebi.fg.core_model.toplevel;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.terms.CVTerm;

/**
 * Used on a number of of entities to be able to attach them a set of generic and typed properties (annotations). 
 * This class goes in tandem with {@link Annotatable}.
 *
 * <dl><dt>Date</dt><dd>Dec 20, 2007</dd></dl>
 *
 * @author Marco Brandizi (original design/implementation take from AE2)
 * 
 */
@Entity
@Inheritance ( strategy = InheritanceType.TABLE_PER_CLASS )
@Table ( name = "annotation" )
public class Annotation extends Identifiable 
{
	private AnnotationType type;
	private String text;
	private CVTerm provenance;
	private Date timestamp;
	private Double score;
	private String notes;
	private String internalNotes;

	
	protected Annotation() {
	}

  public Annotation ( AnnotationType type, String text ) 
  {
  	this.type = type;
  	this.text = text;
  }


  @Lob
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

	@ManyToOne( targetEntity = AnnotationType.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH } )
	@Fetch( FetchMode.JOIN )
	@JoinColumn( name = "type_id", nullable = true )
  public AnnotationType getType() {
    return type;
  }

  public void setType(AnnotationType type) {
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

	public void setProvenance ( CVTerm provenance )
	{
		this.provenance = provenance;
	}

	/**
	 * When this annotation was created or last updated. TODO: Do we need to distinguish between creation/update.
	 */
	public Date getTimestamp ()
	{
		return timestamp;
	}

	public void setTimestamp ( Date timestamp )
	{
		this.timestamp = timestamp;
	}

	
	/**
	 * A measurement of how good or significant this annotation. This can be something like a p-value, or a percentage,
	 * or anything like that. TODO: do we need something like 'scoreType'? For the moment, this can be associated to 
	 * the {@link #getProvenance() provenance} or can be stored into {@link #getInternalNotes() internalNotes}.
	 * 
	 */
	public Double getScore ()
	{
		return score;
	}

	public void setScore ( Double score )
	{
		this.score = score;
	}

	/**
	 * Notes that can possibly be shown to the end-user.
	 */
	public String getNotes ()
	{
		return notes;
	}

	public void setNotes ( String notes )
	{
		this.notes = notes;
	}

	/**
	 * Notes that are technical and are not supposed to be understood by the end user, e.g., computational conditions
	 * stored by the tool that computed this annotation.
	 */
	public String getInternalNotes ()
	{
		return internalNotes;
	}

	public void setInternalNotes ( String internalNotes )
	{
		this.internalNotes = internalNotes;
	}

	/**
   * Equals they've the same texts and the same types. This is to avoid duplication within the same annotatable. 
   */
  @Override
  public boolean equals(Object o) 
  {
    if ( this == o ) return true;
    if ( !(o instanceof Annotation) ) return false;

    Annotation that = (Annotation) o;

    if ( this.getType () != null ? !this.type.equals( that.getType () ) : that.getType () != null ) return false;
    if ( this.getText () != null ? !this.text.equals( that.getText () ) : that.getText () != null ) return false;

    return true;
  }

  @Override
  public int hashCode() 
  {
    int result = getType () == null ? 0 : type.hashCode();
    return 31 * result + ( getText () == null ? 0 : text.hashCode() );
  }

 
  @Override
  public String toString() 
  {
  	return String.format ( 
  		" %s { id: %d, text: '%s', type: %s }", 
  		this.getClass ().getSimpleName (), this.getId (), StringUtils.abbreviate ( this.getText (), 20 ), this.getType () 
  	);
  }
}
