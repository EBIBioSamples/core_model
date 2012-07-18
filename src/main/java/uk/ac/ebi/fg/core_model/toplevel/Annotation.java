package uk.ac.ebi.fg.core_model.toplevel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;

/**
 * Used on a number of of entities to be able to attach them a set of generic typed properties (annotation). This class
 * goes in tandem with {@link Annotatable}. Also see the default implementation for it, {@link DefaultAnnotatable}, 
 * {@link DefaultAccessibleAnnotatable}.
 *
 * <dl><dt>Date</dt><dd>Dec 20, 2007</dd></dl>
 *
 * @author  Nataliya Sklyar
 * @author  Tony Burdett
 * @author  mdylag
 * @author Marco Brandizi (migrated to AE2 and revised in 2012)
 * 
 */
@Embeddable
@SequenceGenerator ( name = "hibernate_seq" )
public class Annotation extends Identifiable 
{
	@ManyToOne( 
		targetEntity = AnnotationType.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, 
		fetch = FetchType.EAGER 
	)
	@Fetch( FetchMode.JOIN )
	@JoinColumn( name = "type_id", nullable = false)
	private AnnotationType type;
	
	// TODO: BLOB?!
	@Column ( name = "text", length = 4000 )
	private String text;

	protected Annotation() {
	}

  public Annotation ( AnnotationType type, String text ) 
  {
  	this.type = type;
  	this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public AnnotationType getType() {
    return type;
  }

  public void setType(AnnotationType type) {
     this.type = type;
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
