/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.xref;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.Validate;

import uk.ac.ebi.fg.core_model.dao.hibernate.toplevel.IdentifiableDAO;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * TODO: Comment me!
 * 
 * <dl><dt>date</dt><dd>Aug 9, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ReferenceSourceDAO<S extends ReferenceSource> extends IdentifiableDAO<S>
{
	public ReferenceSourceDAO ( Class<S> managedClass, EntityManager entityManager ) {
		super ( managedClass, entityManager );
	}
	
	/**
	 * Tells whether a given source exists, based on its accession and version. Note that if the version is null it checks
	 * that there is a record with null version. 
	 */
	public boolean exists ( String accession, String version )
	{
		Validate.notEmpty ( accession, "accession must not be empty" );
		
		// TODO: SQL-injection security
		String hql = "SELECT s.id FROM " + this.getPersistentClass().getCanonicalName() + 
			" s WHERE s.acc = '" + accession + "' AND s.version " + ( version == null ? "IS NULL" : "= '" + version + "'" );
				
		Query query = getEntityManager ().createQuery( hql );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	/**
	 * Works like {@link #exists(String, String)}, but don't check the version, i.e., returns true if there is any version
	 * of the source with the given accession. 
	 * 
	 */
	public boolean exists ( String accession ) 
	{
		// TODO: SQL-injection security
		String hql = "SELECT s.id FROM " + this.getPersistentClass().getCanonicalName() + " s WHERE s.acc = '" + accession;
				
		Query query = getEntityManager ().createQuery( hql );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

	/**
	 * @return if the source's accession and version don't exist yet, returns the same entity, after having attached 
	 * it to the persistence context. If the entity already exists in the DB, returns the copy given by the persistence 
	 * context (i.e., attached to it). Note that the semantics used to search by accession and version is the one explained
	 * in {@link #find(String, String)}.
	 *  
	 */
	public S getOrCreate ( S src ) 
	{
	  Validate.notNull ( src, "Database access error: cannot fetch a null reference-source" );
	  Validate.notEmpty ( src.getAcc(), "Database access error: cannot fetch an accessible with empty accession" );
	
	  S srcDB = find ( src.getAcc(), src.getVersion () );
	  if ( srcDB == null ) 
	  {
	    create ( src );
	    srcDB = src;
	  }
	  return srcDB;
	}

	/**
	 * Finds a source by accession and version. version = null is matched against a record having a null version.
	 */
	public S find ( String accession, String version ) 
	{
	  Validate.notEmpty ( accession, "Database access error: cannot fetch an accessible with empty accession" );
	  
	  // TODO: SQL-injection security
	  String hql = "SELECT s FROM " + this.getPersistentClass().getCanonicalName() + 
	  	" s WHERE s.acc = '" + accession + "' AND s.version " + ( version == null ? "IS NULL" : "= '" + version + "'" );
	
	  Query query = getEntityManager ().createQuery ( hql );
		
		@SuppressWarnings("unchecked")
		List<S> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
	}


  /**
   * Finds all the versions of a source having a given accession. 
   * 
   */
	@SuppressWarnings("unchecked")
  public List<S> find ( String accession ) 
  {
    Validate.notEmpty ( accession, "Database access error: cannot fetch an empty accessible" );
    
		String queryStr= "SELECT s FROM " + this.getPersistentClass().getCanonicalName () + " s WHERE s.acc = ?1";
		Query query = getEntityManager ().createQuery ( queryStr );
		query.setParameter ( 1, accession );
		
		return query.getResultList();
  }

}
