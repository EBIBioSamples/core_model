package uk.ac.ebi.fg.core_model.terms;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Used to assign a type to an {@link AnnotationType}. Values for this usually come from controlled vocabulary (i.e. 
 * enumerations).
 * 
 * <dl><dt>Date</dt><dd>Jan 4, 2008</dd></dl>
 * 
 * @author Nataliya Sklyar
 */
@Entity
@Table ( name = "annotation_type" )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "annotation_type_seq" )
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
