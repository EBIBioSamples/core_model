package uk.ac.ebi.fg.core_model.terms;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.toplevel.Annotation;


/**
 * Used to assign a type to an {@link Annotation}. Values for this usually come from controlled vocabulary (i.e. 
 * enumerations).
 * 
 * <dl><dt>Date</dt><dd>Jan 4, 2008</dd></dl>
 * 
 * @author Nataliya Sklyar
 */
@Entity
@Table ( name = "annotation_type" )
@org.hibernate.annotations.Table ( appliesTo = "annotation_type", 
	indexes = @Index ( name = "ann_type_name", columnNames = "name" ) 
)
public final class AnnotationType extends CVTerm 
{
	protected AnnotationType () {
		super ();
	}

	public AnnotationType ( String name )
	{
		super ( name );
	}
}
