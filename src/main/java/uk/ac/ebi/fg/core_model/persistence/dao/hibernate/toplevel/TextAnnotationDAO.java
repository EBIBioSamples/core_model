package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import javax.persistence.EntityManager;

import uk.ac.ebi.fg.core_model.toplevel.TextAnnotation;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>3 Feb 2015</dd>
 *
 */
public class TextAnnotationDAO extends AnnotationDAO<TextAnnotation>
{
	public TextAnnotationDAO ( EntityManager entityManager )
	{
		super ( TextAnnotation.class, entityManager );
	}

}
