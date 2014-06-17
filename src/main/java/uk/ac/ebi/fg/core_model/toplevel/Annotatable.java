package uk.ac.ebi.fg.core_model.toplevel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

/**
 * An object that can receive a set of {@link Annotation annotations}.
 * 
 * <p><b>Please note</b>: the default implementations you find in this package for this interface defines the annotation
 * relationship as a one-to-many relation that points to a single table to which {@link Annotation} is mapped. 
 * Depending on your data set, yoy may want to adopt an alternative one-table-per-parent approach for Annotation. This 
 * is hard to achieve in JPA/Hibernate, you find an example of that in 
 * {@link uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.RemappedAnnotationTest} in the test 
 * packages.</p>
 *
 * <dl><dt>date</dt><dd>May 31, 2012</dd></dl>
 * @author brandizi
 *
 */
@MappedSuperclass
public class Annotatable extends Identifiable
{
	private Set<Annotation> annotations = new HashSet<Annotation>();
  
  public void addAnnotation ( Annotation annotation ) {
  	annotations.add ( annotation );
  }

  @ManyToMany ( cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
  // By not mentioning the table, one new join table is created per new mapping table
  @JoinTable ( joinColumns = @JoinColumn ( name = "owner_id" ), inverseJoinColumns = @JoinColumn ( name = "annotation_id" ) )
  public Set<Annotation> getAnnotations() {
  	return annotations;
  }

  public void setAnnotations( Set<Annotation> annotations ) {
  	this.annotations = annotations;
  }
}
