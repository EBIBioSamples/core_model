/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms;

import static uk.ac.ebi.utils.sql.SqlUtils.parameterizedWithNullSql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.Validate;

import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.IdentifiableDAO;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * The DAO to access {@link OntologyEntry} entities.
 *
 * <dl><dt>date</dt><dd>Aug 22, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OntologyEntryDAO<OE extends OntologyEntry> extends IdentifiableDAO<OE>
{
	public OntologyEntryDAO ( Class<OE> managedClass, EntityManager entityManager ) {
		super ( managedClass, entityManager );
	}

	/**
	 * Checks for the existence of an OE by means of its accession and its source identifier. 
	 * Note that a null version is matched against a null version in the database, including the srcUrl parameter
	 *  
	 */
	public boolean contains ( String accession, String srcAcc, String srcVer, String srcUrl ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc AND oe.source.acc = :srcAcc AND "  + parameterizedWithNullSql ( "source.version", "srcVer" )
			+ " AND " + parameterizedWithNullSql ( "source.url", "srcUrl" );
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc )
			.setParameter ( "srcVer", srcVer )
			.setParameter ( "srcUrl", srcUrl );
			
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	
	/**
	 * Check for the existence of an OE, independently on the source URL
	 */
	public boolean contains ( String accession, String srcAcc, String srcVer  ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc AND oe.source.acc = :srcAcc AND "  + parameterizedWithNullSql ( "source.version", "srcVer" );
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc )
			.setParameter ( "srcVer", srcVer );
			
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	
	
	/**
	 * Same as {@link #contains(String, String, String)}, but checks any source version/URL.
	 */
	public boolean contains ( String accession, String srcAcc ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() 
			+ " oe WHERE oe.acc = :acc AND oe.source.acc = :srcAcc";
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	
	
	/**
	 * Searches for an oe via {@link #find(String, String, String)}. Returns a DB record if it finds something,
	 * attaches the parameter to the persistence context if not. 
	 * 
	 */
	public OE getOrCreate ( OE oe ) 
	{
	  Validate.notNull ( oe, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( oe.getAcc(), "Database access error: cannot fetch an ontology entry with empty accession" );
	  
	  ReferenceSource src = oe.getSource ();
	  Validate.notNull ( src, "Database access error: cannot fetch an ontology entry with null reference-source" );
	  Validate.notEmpty ( src.getAcc(), "Database access error: cannot fetch an ontology entry with with an empty-accession ref source" );
	  
	  OE xdb = find ( oe.getAcc (), src.getAcc(), src.getVersion (), src.getUrl () );
	  if ( xdb == null ) {
	    create ( oe );
	    xdb = oe;
	  }
	  return xdb;
	}

	/**
	 * Finds an ontology entry by means of its accession and its source identifier. 
	 * Note that a null version is matched against a null version in the database, the same for the source URL.
	 *  
	 */
	public OE find ( String accession, String srcAcc, String srcVer, String srcUrl ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc AND oe.source.acc = :srcAcc AND " + parameterizedWithNullSql ( "oe.source.version", "srcVer" )
			+ " AND " + parameterizedWithNullSql ( "oe.source.url", "srcUrl" );
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc )
			.setParameter ( "srcVer", srcVer )
			.setParameter ( "srcUrl", srcUrl );
		
		@SuppressWarnings("unchecked")
		List<OE> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
	}
	
	@SuppressWarnings("unchecked")
	public List<OE> find ( String accession, String srcAcc, String srcVer ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc AND oe.source.acc = :srcAcc AND " + parameterizedWithNullSql ( "oe.source.version", "srcVer" );
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc )
			.setParameter ( "srcVer", srcVer );
		
		return query.getResultList();
	}
	
	/**
	 * Same as {@link #find(String, String, String)} but finds any version.
	 */
	@SuppressWarnings ( "unchecked" )
	public List<OE> find ( String accession, String srcAcc ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc AND " + parameterizedWithNullSql ( "oe.source.acc", "srcAcc" );
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "srcAcc", srcAcc );
		
		return query.getResultList();
	}

}
