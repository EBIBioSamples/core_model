package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
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
			"SELECT a.id FROM " + targetClass.getCanonicalName () + " a WHERE a.acc = :acc");
		query.setParameter ( "acc", accession );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

  /**
   * A wrapper of {@link #find(String, Class)} that uses {@link #getManagedClass()}.
   */
	public boolean contains ( String accession ) {
		return contains ( accession, this.getManagedClass () );
	}
	
	
	/**
	 * @return if the entity's accession doesn't exist yet, returns the same entity, but after having attached it to 
	 * the persistence context. If the entity already exists in the DB, returns the copy given by the persistence 
	 * context (i.e., attached to it).
	 * 
	 */
	public A getOrCreate ( A accessible ) 
	{
	
	  Validate.notNull ( accessible, "Database access error: cannot fetch a null accessible" );
	  String acc = accessible.getAcc ();
	  Validate.notEmpty ( acc, "Database access error: cannot fetch an accessible with empty accession" );
	
	  A accDB = find ( acc );
	  if ( accDB == null ) 
	  {
	    create ( accessible );
	    accDB = accessible;
	  }
	  return accDB;
	}

	/** 
	 * Works like {@link IdentifiableDAO#mergeBean(uk.ac.ebi.fg.core_model.toplevel.Identifiable)}, but searches
	 * by {@link #find(String) accession}.
	 */
	@Override
  public A mergeBean ( A accessible )
  {
	  Validate.notNull ( accessible, "Database access error: cannot fetch a null accessible" );
	  String acc = accessible.getAcc ();
	  Validate.notNull ( acc, "Database access error: cannot fetch an entity with empty accessible" );

	  A accDB = find ( acc );
	  if ( accDB == null ) 
	  {
	    create ( accessible );
	    return accessible;
	  }
	  
	  return mergeBeanHelper ( accessible, accDB ); // we only need this, the rest will come with transaction's commit.
  }
	
  /**
   * Finds by accession, optionally allows for the search of a specific A's subclass. 
   */
  public A find ( String accession, Class<? extends A> targetClass ) 
  {
    Validate.notEmpty ( accession, "Database access error: cannot fetch an empty accessible" );
    
		String queryStr= "SELECT a FROM " + targetClass.getCanonicalName () + " a WHERE a.acc = :acc";
		Query query = getEntityManager ().createQuery ( queryStr );
		query.setParameter ( "acc", accession );
		
		@SuppressWarnings("unchecked")
		List<A> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
  }

  /**
   * A wrapper of {@link #find(String, Class)} that uses {@link #getManagedClass()}.
   */
  public A find ( String accession ) {
  	return find ( accession, this.getManagedClass () );
  } 
  
	/**
	 * Like {@link #find(String)}, but fails if it doesn't find anything.
	 */
	public A findAndFail ( String accession, Class<? extends A> targetClass ) 
	{
		A result = find ( accession );
		if ( result == null ) throw new IllegalArgumentException ( "'" + accession + "' not found" );
		return result;
	}

	public A findAndFail ( String accession ) 
	{
		return findAndFail ( accession, this.getManagedClass () );
	}

	
  public boolean delete ( String accession ) 
  {
  	accession = StringUtils.trimToNull ( accession );
  	Validate.notNull ( "Cannot delete with a null accession" );
  		
  	A accessible = find ( accession );
  	if ( accessible == null ) return false;
  	
  	return this.delete ( accessible );
  }
}
