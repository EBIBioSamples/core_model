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
 * The interface between the core model and the relational database it is mapped to is managed via the Data Access Object 
 * design pattern. This is a base DAO, from which all the others are derived. All DAOs in this package manages the storage
 * via Hibernate and JPA annotations.
 * 
 * Several methods defined here were initially based on the PerfectJPattern library, which seems now dead and it's not compatible
 * with Hibernate >= 4.3. TODO: extract a more generic DAO.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class IdentifiableDAO<T extends Identifiable> // extends AbstractHibernateManagedGenericDao<Long, D>
{
	private EntityManager entityManager; 
	private final Class<T> managedClass;
	
	public IdentifiableDAO ( Class<T> managedClass, EntityManager entityManager ) 
	{
		this.managedClass = managedClass;
		this.setEntityManager ( entityManager );
	}
	
	public long count ()
	{
		Criteria criteria = ((Session) entityManager.getDelegate ()).createCriteria ( getManagedClass() ).
		setProjection ( Projections.rowCount() ).setCacheMode ( CacheMode.IGNORE );
		
		// This is the JPA 2.0 way
		Long value = (Long) criteria.list().get( 0 );
		return value;
	}

	/**
	 * Creates an entity using {@link #getEntityManager()}.persist().
	 */
  public void create ( T entity ) 
  {
    Validate.notNull ( entity, "Internal error: 'entity' must not be null" );
  	entityManager.persist ( entity );
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
  
  
  /**
   * Deletes an entity using {@link #getEntityManager()}.remove() and returns true if it was there and actually deleted.
   */
  public boolean delete( T entity ) 
  {
    Validate.notNull ( entity, "Internal error: 'entity' must not be null" );
    if ( !entityManager.contains ( entity ) ) return false;
    entityManager.remove ( entity );
    return true;
  }
  
  /** Finds a arbitrary identifiable. */ 
  public <T1 extends T> T1 find ( long id, Class<T1> targetClass )
  {
  	return entityManager.find ( targetClass, id );
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
   * Queries by example, using {@link Example}. Method inspired to PerfectJPattern.
   * 
   * @param example: the entity-bean to be used as example, all its non-null properties that are not listed in  excludedProperties
   * are used as AND search facet.
   * 
   * @param likeMode: if true, searches by wrapping all the included parameters with '%' and searches via LIKE, i.e., 
   * searches partial strings.
   * 
   * @param excludedProperties: bean properties you want to ignore even if they're non-null. These must follow bean naming
   * conventions (e.g., "name" for "getName").
   * 
   */
	@SuppressWarnings ( "unchecked" )
	public List<T> findByExample ( T example, boolean likeMode, String ... excludedProperties )
  {
	  Validate.notNull ( example, "Internal error: findByExample() invoked with null parameter" );        
	  
		Session session = (Session) entityManager.getDelegate ();            
	    
	  Example exCriterion = Example.create ( example );
	  if ( likeMode ) exCriterion.enableLike ( MatchMode.ANYWHERE );
	  
	  if ( excludedProperties != null )
	  	for ( String myPropertyName : excludedProperties )
	  		exCriterion.excludeProperty ( myPropertyName );
	  
	  Criteria criteria = session.createCriteria( getManagedClass() );
	  criteria.add ( exCriterion );
	  
	  return criteria.list ();            
  }
	
	/** Searches with enableLike = false */
	public List<T> findByExample ( T example, String ... excludedProperties ) {
		return findByExample ( example, false, excludedProperties );
	}
	
	
	/**
	 * The class that this DAO manages, which was passed via the constructor.
	 * @return
	 */
	public Class<T> getManagedClass ()
	{
		return this.managedClass;
	}

	/**
	 * The JPA/Hibernate entity manager used to interface the storage back-end and perform operations.
	 * Note that DAOs never deals with transactions, you should get the entity manager from this method, start a 
	 * transaction via {@link EntityManager#getTransaction()} and then invoke DAO's methods. 
	 *  
	 */
	public EntityManager getEntityManager ()
	{
		return entityManager;
	}

	public void setEntityManager ( EntityManager entityManager )
	{
		this.entityManager = entityManager;
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
