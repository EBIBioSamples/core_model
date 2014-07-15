package uk.ac.ebi.fg.core_model.toplevel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.apache.commons.lang.StringUtils;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>23 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "text" )
public class TextAnnotation extends Annotation
{
	private String text;

	protected TextAnnotation () {
	}
	
  public TextAnnotation ( AnnotationType type, String text ) 
  {
  	super();
  	this.setType ( type );
  	this.text = text;
  }


  @Lob
  public String getText() {
    return text;
  }

  protected void setText(String text) {
    this.text = text;
  }


	/**
   * Equals they've the same texts and the same types. This is to avoid duplication within the same annotatable. 
   */
  @Override
  public boolean equals(Object o) 
  {
    if ( this == o ) return true;
    if ( !(o instanceof Annotation) ) return false;

    TextAnnotation that = (TextAnnotation) o;
    
    AnnotationType type = this.getType ();

    if ( type != null ? !type.equals( that.getType () ) : that.getType () != null ) return false;
    if ( this.getText () != null ? !this.text.equals( that.getText () ) : that.getText () != null ) return false;

    return true;
  }

  @Override
  public int hashCode() 
  {
    AnnotationType type = this.getType ();
    int result = type == null ? 0 : type.hashCode();
    return 31 * result + ( getText () == null ? 0 : text.hashCode() );
  }
  
	@Override
  public String toString () 
  {
  	return String.format ( 
  		" %s { id: %d, text: '%s', type: %s, timestamp: %tc, provenance: %s, score: %f, notes: '%s', internalNotes: '%s' }", 
  		this.getClass ().getSimpleName (), this.getId (), StringUtils.abbreviate ( this.getText (), 20 ), this.getType (),
  		this.getTimestamp (), this.getProvenance (), this.getScore (), StringUtils.abbreviate ( this.getNotes (), 20 ), 
  		StringUtils.abbreviate ( this.getInternalNotes (), 20 )
  	);
  }

}
