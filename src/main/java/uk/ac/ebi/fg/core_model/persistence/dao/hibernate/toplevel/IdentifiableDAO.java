package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.Validate;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
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
		
		//JPA 2.0
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
   * Deletes an entity using {@link #getEntityManager()}.remove() and returns true if it was there and actually deleted.
   */
  public boolean delete( T entity ) 
  {
    Validate.notNull ( entity, "Internal error: 'entity' must not be null" );
    if ( !entityManager.contains ( entity ) ) return false;
    entityManager.remove ( entity );
    return true;
  }
  
  
  public T findById ( Long id )
  {
  	return entityManager.find ( managedClass, id );
  }
  
  /**
   * Queries by example, using {@link Example}. Method copied from PerfectJPattern.
   * 
   * @param example the entity-bean to be used as example, all its non-null properties that are not listed in  excludedProperties
   * are used as AND search facet.
   * 
   * @param excludedProperties bean properties you want to ignore even if they're non-null. These must follow bean naming
   * conventions (e.g., "name" for "getName").
   * 
   */
	@SuppressWarnings ( "unchecked" )
	public List<T> findByExample ( T example, String ... excludedProperties)
  {
	  Validate.notNull ( example, "Internal error: findByExample() invoked with null parameter" );        
	  Validate.notNull ( excludedProperties, "Internal error: findByExample() invoked with null exclusion list" );
	  
	  List<T> myElements = null;
	  
		Session mySession = (Session) entityManager.getDelegate ();            
	    
	  Criteria myCriteria = mySession.createCriteria( getManagedClass() );
	  Example myExample = Example.create ( example );
	  for ( String myPropertyName : excludedProperties )
	  	myExample.excludeProperty ( myPropertyName );
	  
	  myElements = myCriteria.add ( myExample ).list();
	  
	  return myElements;            
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
}
