package uk.ac.ebi.fg.core_model.toplevel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import uk.ac.ebi.fg.core_model.organizational.Contact;

/**
 * A default implementation for {@link Annotatable}. You can use this for those Annotatable(s) that needs only to 
 * extend {@link Identifiable}.
 * 
 * <dl><dt>date</dt><dd>Jun 5, 2012</dd></dl>
 * @author brandizi
 *
 */
@MappedSuperclass
public class DefaultAnnotatable extends Identifiable implements Annotatable
{
	/**
	 * You need to change the table name with {@link AssociationOverride#joinTable()}. Have a look at {@link Contact}
	 * for an example.
	 */
  @Embedded
	@ElementCollection
	@CollectionTable( name = "annotatable_annotation", joinColumns = @JoinColumn( name = "owner_id" ) )
	private Set<Annotation> annotations = new HashSet<Annotation>();
  
  @Override
  public void addAnnotation ( Annotation annotation ) {
  	annotations.add ( annotation );
  }

  @Override
  public Set<Annotation> getAnnotations() {
  	return annotations;
  }

  @Override
  public void setAnnotations( Set<Annotation> annotations ) {
  	this.annotations = annotations;
  }
}
