package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.Validate;

import uk.ac.ebi.fg.core_model.toplevel.Accessible;

/**
 * Generic Accessible DAO.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 * @param <A>
 */
public class AccessibleDAO<A extends Accessible> extends IdentifiableDAO<A>
{
	public AccessibleDAO ( Class<A> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}

  /**
   * Optionally allows for the search of a specific A's subclass.
   * TODO: There are other DAOs that need this targetClass parameter.
   *  
   */
	public boolean contains ( String accession, Class<? extends A> targetClass )
	{
		Validate.notEmpty ( accession, "accession must not be empty");
		 
		Query query = getEntityManager ().createQuery(
			"SELECT a.id FROM " + targetClass.getCanonicalName () + " a WHERE a.acc = ?1");
		query.setParameter ( 1, accession );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

  /**
   * A wrapper of {@link #find(String, Class)} that uses {@link #getPersistentClass()}.
   */
	public boolean contains ( String accession ) {
		return contains ( accession, this.getPersistentClass () );
	}
	
	
	/**
	 * @return if the entity's accession doesn't exist yet, returns the same entity, but after having attached it to 
	 * the persistence context. If the entity already exists in the DB, returns the copy given by the persistence 
	 * context (i.e., attached to it).
	 *  
	 */
	public A getOrCreate ( A entity ) 
	{
	
	  Validate.notNull ( entity, "Database access error: cannot fetch a null accessible" );
	  Validate.notEmpty ( entity.getAcc(), "Database access error: cannot fetch an accessible with empty accession" );
	
	  A accessible = find ( entity.getAcc() );
	  if ( accessible == null ) 
	  {
	    create ( entity );
	    accessible = entity;
	  }
	  return accessible;
	}

  /**
   * Finds by accession, optionally allows for the search of a specific A's subclass. 
   */
  public A find ( String accession, Class<? extends A> targetClass ) 
  {
    Validate.notEmpty ( accession, "Database access error: cannot fetch an empty accessible" );
    
		String queryStr= "SELECT a FROM " + targetClass.getCanonicalName () + " a WHERE a.acc = ?1";
		Query query = getEntityManager ().createQuery ( queryStr );
		query.setParameter ( 1, accession );
		
		@SuppressWarnings("unchecked")
		List<A> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
  }

  /**
   * A wrapper of {@link #find(String, Class)} that uses {@link #getPersistentClass()}.
   */
  public A find ( String accession ) {
  	return find ( accession, this.getPersistentClass () );
  } 
  
}
