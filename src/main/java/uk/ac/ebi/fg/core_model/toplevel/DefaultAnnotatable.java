package uk.ac.ebi.fg.core_model.toplevel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * A default implementation for {@link Annotatable}. You can use this for those Annotatable(s) that needs only to 
 * extend {@link Identifiable}. @see {@link Annotatable} for notes on the references relationship.
 * 
 * <dl><dt>date</dt><dd>Jun 5, 2012</dd></dl>
 * @author brandizi
 *
 */
@MappedSuperclass
public class DefaultAnnotatable extends Identifiable implements Annotatable
{
	private Set<Annotation> annotations = new HashSet<Annotation>();
  
  @Override
  public void addAnnotation ( Annotation annotation ) {
  	annotations.add ( annotation );
  }

  @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
  @JoinTable ( joinColumns = @JoinColumn ( name = "owner_id" ), inverseJoinColumns = @JoinColumn ( name = "annotation_id" ) )
  @Override
  public Set<Annotation> getAnnotations() {
  	return annotations;
  }

  @Override
  public void setAnnotations( Set<Annotation> annotations ) {
  	this.annotations = annotations;
  }
}
