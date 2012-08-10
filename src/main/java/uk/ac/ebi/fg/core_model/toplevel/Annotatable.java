package uk.ac.ebi.fg.core_model.toplevel;

import java.util.Set;


/**
 * An object that can receive a set of {@link Annotation annotations}.
 * 
 * <p><b>Please note</b>: the default implementations you find in this package for this interface defines the annotation
 * relationship as a one-to-many relation that points to a single table to which {@link Annotation}. 
 * Depending on your data set, yoy may want to adopt an alternative one-table-per-parent approach for Annotation. This 
 * is hard to achieve in JPA/Hibernate, you find an example of that in {@link RemappedAnnotationTest} in the test 
 * packages.</p>
 *
 * <dl><dt>date</dt><dd>May 31, 2012</dd></dl>
 * @author brandizi, imported from AE2.
 *
 */
public interface Annotatable 
{
	  public Set<Annotation> getAnnotations();
	  public void addAnnotation ( Annotation annotation );
	  public void setAnnotations ( Set<Annotation> annotations );
}
