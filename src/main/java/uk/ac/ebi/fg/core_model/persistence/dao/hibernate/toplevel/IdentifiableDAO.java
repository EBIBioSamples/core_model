package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import uk.ac.ebi.fg.core_model.toplevel.Identifiable;


/**
 * @see AbstractDAO
 * 
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class IdentifiableDAO<T extends Identifiable> extends AbstractDAO<T> 
{
	public IdentifiableDAO ( Class<T> managedClass, EntityManager entityManager ) 
	{
		super ( managedClass, entityManager );
	}
  

	/** 
	 * First does a search of the entity, via {@link #find(long) ID}, then, if anything is found, merges all the non-null 
	 * attributes into the database entity and saves the updated object. Note that this is different than a simple
	 * {@link EntityManager#merge(Object)}, since here we ignore the bean properties in the parameter that have null values, 
	 * we keep the values from the DB in such a case. 
	 * 
	 * @return the newly saved object.
	 * 
	 */
  public T mergeBean ( T entity )
  {
	  Validate.notNull ( entity, "Database access error: cannot fetch a null accessible" );
	  Validate.notNull ( entity.getId (), "Database access error: cannot fetch an entity with empty ID" );

	  T entDB = find ( entity.getId () );
	  if ( entDB == null ) 
	  {
	    create ( entity );
	    return entity;
	  }

	  return mergeBeanHelper ( entity, entDB ); // we only need this, the rest will come with transaction's commit.
  }
  
  
  
  /** Finds a arbitrary identifiable. */ 
  public <T1 extends T> T1 find ( long id, Class<T1> targetClass )
  {
  	return getEntityManager ().find ( targetClass, id );
  }

  /** Finds an identifiable instance of {@link #getManagedClass()}. */
  public T find ( long id ) {
  	return find ( id, this.getManagedClass () );
  }

  /** Finds a arbitrary identifiable, this is slightly more performant than {@link #find(long, Class)}. */ 
  public <T1 extends T> boolean contains ( long id, Class<T1> targetClass )
  {
		Query query = getEntityManager ().createQuery(
			"SELECT i.id FROM " + targetClass.getCanonicalName () + " i WHERE i.id = ?1");
		query.setParameter ( 1, id );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
  }
  
  /** Finds an identifiable instance of {@link #getManagedClass()}, this is slightly more performant than 
   * {@link #find(long, Class)}. */ 
  public boolean contains ( long id ) {
  	return contains ( id, this.getManagedClass () );
  }
	
	/**
	 * Does the merge of src into dest, as explained in {@link #mergeBean(Identifiable)}. Put here, cause it
	 * is needed in {@link AccessibleDAO} as well.
	 * 
	 */
	public static <T extends Identifiable> T mergeBeanHelper ( T src, T dest )
	{
	  for ( PropertyDescriptor pd: PropertyUtils.getPropertyDescriptors ( src ) )
	  {
	  	Exception theEx = null;
	  	try {
	  		Object pval = pd.getReadMethod ().invoke ( src );
	  		Method setter = pd.getWriteMethod ();
	  		if ( setter == null || ( setter.getModifiers () | Modifier.PUBLIC ) == 0 ) continue;
	  		if ( pval == null ) continue;
	  		if ( pval instanceof Collection && ((Collection<?>) pval).isEmpty () ) continue;
				BeanUtils.copyProperty ( dest, pd.getName (), pval );
			} 
	  	catch ( IllegalArgumentException ex ) { theEx = ex; }
			catch ( IllegalAccessException ex ) { theEx = ex; }
			catch ( InvocationTargetException ex ) { theEx = ex; }
	  	finally {
	  		if ( theEx != null ) throw new RuntimeException ( "Internal error while saving to database: " + theEx.getMessage (), theEx );
	  	}
	  }
		
	  return dest;
	}
}
