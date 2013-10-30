package uk.ac.ebi.fg.core_model.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.IdentityHashMap;

import org.apache.commons.beanutils.PropertyUtils;

import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * Resets all the {@link Identifiable#getId() IDs} in an object tree to null. This is useful when you want to re-attempt a 
 * persistence operation, as though all the objects are new. Hibernate complaints when an ID is non null (and not attached
 * to its session), cause it believes it comes from the DB and has to update it.
 *
 * <dl><dt>date</dt><dd>30 Oct 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class IdCleaner
{
	private IdentityHashMap<Identifiable, Identifiable> visited = new IdentityHashMap<Identifiable, Identifiable> ();

	/**
	 * Goes through all the {@link Identifiable} that can be reached by the parameter JavaBean getters and resets all 
	 * of their {@link Identifiable#getId() ID} to null, including the parameter itself.
	 * 
	 * Such job is actually done by {@link #resetIDsRecursion(Identifiable)}, this method is just a wrapper that resets
	 * the internal tracker of already-visited objects.
	 * 
	 */
	public void resetIDs ( Identifiable ent )
	{
		visited.clear ();
		resetIDsRecursion ( ent );
	}

	/**
	 * Goes through all the {@link Identifiable} that can be reached by the parameter JavaBean getters and resets all 
	 * of their {@link Identifiable#getId() ID} to null, including the parameter itself.
	 * 
	 * This method also tracks the objects it visits and avoids loops.
	 */
	private void resetIDsRecursion ( Identifiable ent )
	{
		if ( ent == null ) return;
		if ( visited.containsKey ( ent ) ) return;
		visited.put ( ent, ent );
		
		Exception theEx = null;
		try
		{
			// Make the ID null
			Method idSetter = Identifiable.class.getDeclaredMethod ( "setId", Long.class );
			idSetter.setAccessible ( true );
			idSetter.invoke ( ent, (Long) null );
			
			// Go through all the JavaBean properties
			for ( PropertyDescriptor pd: PropertyUtils.getPropertyDescriptors ( ent ) )
			{
				Class<?> pt = pd.getPropertyType ();

				// Is the getter a collection or an Identifiable?
				boolean isColl = Collection.class.isAssignableFrom ( pt );
				if ( !( isColl || Identifiable.class.isAssignableFrom ( pt ) ) ) continue;

				Method getter = pd.getReadMethod ();
				if ( getter == null ) continue;
				
				Object pval = getter.invoke ( ent );
				
				if ( isColl )
					// In case of a collection, go through its content
					for ( Object child: (Collection<?>) pval )
						if ( child instanceof Identifiable ) resetIDsRecursion ( (Identifiable) child );
				else
					// Not a collection, so go straight to the property value
					resetIDsRecursion ( (Identifiable) pval );
			}
		} 
		catch ( SecurityException ex ) { theEx = ex; }
		catch ( NoSuchMethodException ex ) { theEx = ex; }
		catch ( IllegalArgumentException ex ) { theEx = ex; }
		catch ( IllegalAccessException ex ) { theEx = ex; }
		catch ( InvocationTargetException ex ) { theEx = ex; }
		finally {
			if ( theEx != null )
				throw new RuntimeException ( "Internal error while resetting ID for " + ent + ": " + theEx.getMessage (), theEx );
		}
	}

}
