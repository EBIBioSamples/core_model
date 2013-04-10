package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.Validate;

import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.IdentifiableDAO;
import uk.ac.ebi.fg.core_model.terms.CVTerm;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;

/**
 * {@link CVTerm} DAO. WARNING: Do not use this for {@link OntologyEntry}, because in the latter the name property
 * is not unique. 
 *
 * <dl><dt>date</dt><dd>Aug 22, 2012</dd></dl>
 * @author Marco Brandizi
 * 
 */
public class CVTermDAO<T extends CVTerm> extends IdentifiableDAO<T>
{
	public CVTermDAO ( Class<T> managedClass, EntityManager entityManager )
	{
		super ( managedClass, entityManager );
	}

	public boolean contains ( String name ) {
		return contains ( name, this.getManagedClass () );
	}
	
	/**
	 * Allows to lookup a specific subclass.
	 */
	public boolean contains ( String name, Class<? extends T> targetClass )
	{
		Validate.notEmpty ( name, "name must not be empty");
		 
		Query query = getEntityManager ().createQuery(
			"SELECT t.id FROM " + targetClass.getCanonicalName () + " t WHERE t.name = ?1");
		query.setParameter ( 1, name );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

	/**
	 * @return if the term's name doesn't exist yet, returns the same term, but after having attached it to 
	 * the persistence context. If the term already exists in the DB, returns the copy given by the persistence 
	 * context (i.e., attached to it).
	 *  
	 */
	public T getOrCreate ( T cvterm ) 
	{
	
	  Validate.notNull ( cvterm, "Database access error: cannot fetch a null cvterm" );
	  Validate.notEmpty ( cvterm.getName (), "Database access error: cannot fetch a term with empty name" );
	
	  T cvtermDB = find ( cvterm.getName () );
	  if ( cvtermDB == null ) 
	  {
	    create ( cvterm );
	    cvtermDB = cvterm;
	  }
	  return cvtermDB;
	}

	
	/**
	 * Wraps {@link #find(String, Class)} with the managed class.
	 * @param name
	 * @return
	 */
  public T find ( String name ) {
  	return find ( name, this.getManagedClass () );
  } 
  
  /**
   * Finds by name, allows to find a sub-class.
   * 
   */
  @SuppressWarnings ( "unchecked" )
	public T find ( String name, Class<? extends T> targetClass ) 
  {
    Validate.notEmpty ( name, "Database access error: cannot fetch a term with empty name" );
    
		String queryStr= "SELECT t FROM " + targetClass.getCanonicalName () + " t WHERE t.name = ?1";
		Query query = getEntityManager ().createQuery ( queryStr );
		query.setParameter ( 1, name );
		
		List<T> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
  }
}
