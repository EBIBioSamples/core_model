package uk.ac.ebi.fg.core_model.toplevel;

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

/**
 * Used on a number of of entities to be able to attach them a set of generic typed properties (annotations). This class
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
@Entity
@Inheritance ( strategy = InheritanceType.TABLE_PER_CLASS )
@Table ( name = "annotation" )
public class Annotation extends Identifiable 
{
	private AnnotationType type;
	private String text;

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
	@JoinColumn( name = "type_id", nullable = false )
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
