package uk.ac.ebi.fg.core_model.toplevel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.StringUtils;

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
@NamedQueries ({
	// Finds the ontology terms annotated with certain parameters
	// TODO: This is used by the BioSD feature annotator and should be moved there (but JPA makes it complicated)
	// Note that Oracle doesn't like CLOBs used in SELECT DISTINCT
	@NamedQuery ( name = "findOntoAnnotations", 
		query = "SELECT DISTINCT ot, ann.score FROM OntologyEntry ot JOIN ot.annotations ann\n"
		+ "WHERE ann.provenance.name = :provenance AND ann.class = 'text'\n"
		+ "  AND STR ( ann.text ) = :annotation\n"
		+ "ORDER BY ann.score DESC" 
	),
	
	@NamedQuery ( name = "findOe2BePurged", 
		query = "FROM OntologyEntry oe WHERE\n"
		+ "oe IN ( SELECT oe1.id FROM OntologyEntry oe1 JOIN oe1.annotations ann WHERE ann.id = :annId )\n"
		+ "AND oe NOT IN ( "
		+ "  SELECT oe1.id FROM OntologyEntry oe1 JOIN oe1.annotations ann1 JOIN ann1.type AS atype1 JOIN ann1.provenance AS prov1"
		+ "    WHERE atype1.name <> :atype OR prov1.name <> :prov OR ann1.timestamp < :startTime OR ann1.timestamp > :endTime"
		+ ")"	
	)
})
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
