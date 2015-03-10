package uk.ac.ebi.fg.core_model.utils.toplevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import uk.ac.ebi.fg.core_model.terms.AnnotationType;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;
import uk.ac.ebi.fg.core_model.toplevel.TextAnnotation;
import uk.ac.ebi.fg.core_model.xref.XRef;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>16 Sep 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class AnnotationUtils
{
	/**
	 * Finds all annotations having a type label like the parameter. If not found, it returns an empty collection.
	 */
	public static List<Annotation> find ( 
		Collection<Annotation> annotations, String value, String type, boolean isCaseSensitive, boolean onlyFirst 
	)
	{
		List<Annotation> result = new ArrayList<Annotation> ();
		
		if ( annotations == null ) return result;
		
		for ( Annotation ann: annotations )
		{
			if ( type != null ) 
			{
				AnnotationType atype = ann.getType ();
				if ( atype == null ) continue;
				String atypeStr = atype.getName ();
				if ( atypeStr == null ) continue;
				if ( ! ( isCaseSensitive ? StringUtils.equalsIgnoreCase ( type, atypeStr ) : type.equals ( atypeStr ) ) )
					continue;
			}
			
			if ( value == null ) 
			{
				result.add ( ann );
				if ( onlyFirst ) break;
				continue;
			}
			
			String aval = ann instanceof TextAnnotation 
				? ((TextAnnotation) ann ).getText ()
				: ann instanceof XRef 
				  ? ((XRef) ann).getAcc ()
				  : null;
				 
		  if ( aval == null ) continue;
				
			if ( ! ( isCaseSensitive ? StringUtils.equalsIgnoreCase ( value, aval ) : value.equals ( aval ) ) )
				continue;
			
			result.add ( ann );
			if ( onlyFirst ) break;
		}
		
		return result;
	}
	
	public static List<Annotation> find ( Collection<Annotation> annotations, String value, String type, boolean isCaseSensitive )
	{
		return find ( annotations, value, type, isCaseSensitive, false );
	}
	
	public static List<Annotation> find ( Collection<Annotation> annotations, String value, String type )
	{
		return find ( annotations, value, type, false );
	}
}
