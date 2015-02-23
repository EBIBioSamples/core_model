package uk.ac.ebi.fg.core_model.toplevel;

import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.terms.CVTerm;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>14 Jul 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "annotation_prov" )
public class AnnotationProvenance extends CVTerm
{
	protected AnnotationProvenance ()
	{
		super ();
	}

	public AnnotationProvenance ( String label )
	{
		super ( label );
	}
}