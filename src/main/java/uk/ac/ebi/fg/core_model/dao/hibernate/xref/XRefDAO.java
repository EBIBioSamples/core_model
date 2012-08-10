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
import uk.ac.ebi.fg.core_model.xref.XRef;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class XRefDAO<X extends XRef> extends IdentifiableDAO<X>
{
	public XRefDAO ( Class<X> managedClass, EntityManager entityManager ) {
		super ( managedClass, entityManager );
	}

	/**
	 * Checks for the existence of a cross-reference by means of its accession and its source identifier. 
	 * Note that a null version is matched against a null version in the database.
	 *  
	 */
	public boolean exists ( String accession, String srcAcc, String srcVer ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null x-ref" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an accessible with empty accession" );
		
		// TODO: SQL-injection security
		String hql = "SELECT x.id FROM " + this.getPersistentClass().getCanonicalName() + 
			" x WHERE x.acc = '" + accession + "' AND x.source AND x.version " + 
			( srcVer == null ? "IS NULL" : "= '" + srcVer + "'" );
				
		Query query = getEntityManager ().createQuery( hql );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	/**
	 * Searches for a cross-reference via {@link #find(String, String, String)}. Returns a DB record if it finds something,
	 * attaches the parameter to the persistence context if not. 
	 * 
	 */
	public X getOrCreate ( X xref ) 
	{
	  Validate.notNull ( xref, "Database access error: cannot fetch a null x-ref" );
	  Validate.notEmpty ( xref.getAcc(), "Database access error: cannot fetch an accessible with empty accession" );
	  
	  ReferenceSource src = xref.getSource ();
	  Validate.notNull ( src, "Database access error: cannot fetch a x-ref with null reference-source" );
	  Validate.notEmpty ( src.getAcc(), "Database access error: cannot fetch an x-ref with with an empty-accession ref source" );
	  
	  X xdb = find ( xref.getAcc (), src.getAcc(), src.getVersion () );
	  if ( xdb == null ) {
	    create ( xref );
	    xdb = xref;
	  }
	  return xdb;
	}

	/**
	 * Finds a cross-reference by means of its accession and its source identifier. 
	 * Note that a null version is matched against a null version in the database.
	 *  
	 */
	public X find ( String accession, String srcAcc, String srcVer ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null x-ref" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an accessible with empty accession" );
		
		// TODO: SQL-injection security
		String hql = "SELECT x FROM " + this.getPersistentClass().getCanonicalName() + 
			" x WHERE x.acc = '" + accession + "' AND x.source AND x.version " + 
			( srcVer == null ? "IS NULL" : "= '" + srcVer + "'" );
				
		Query query = getEntityManager ().createQuery( hql );
		
		@SuppressWarnings("unchecked")
		List<X> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
	}
}
