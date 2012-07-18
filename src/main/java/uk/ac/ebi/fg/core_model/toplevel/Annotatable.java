package uk.ac.ebi.fg.core_model.toplevel;

import java.util.Set;

/**
 * An object that can receive a set of {@link Annotation annotations}.
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
