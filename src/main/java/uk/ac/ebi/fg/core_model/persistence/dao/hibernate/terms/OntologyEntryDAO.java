package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.terms;

import static uk.ac.ebi.utils.sql.HqlUtils.parameterizedWithNullHql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.Validate;

import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.AnnotatableDAO;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * The DAO to access {@link OntologyEntry} entities.
 *
 * <dl><dt>date</dt><dd>Aug 22, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OntologyEntryDAO<OE extends OntologyEntry> extends AnnotatableDAO<OE>
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
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc\n";
		
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n"
	    	+ " AND " + parameterizedWithNullHql ( "oe.source.version", "srcVer" ) + "\n" 
	    	+ " AND " + parameterizedWithNullHql ( "source.url", "srcUrl" ) + "\n";
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query
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
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc";
	
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n"
	    	+ " AND " + parameterizedWithNullHql ( "oe.source.version", "srcVer" ) + "\n"; 
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query
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
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() 
			+ " oe WHERE oe.acc = :acc";
		
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n";
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query.setParameter ( "srcAcc", srcAcc );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

	
	public boolean contains ( String accession ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	//TODO remove Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe.id FROM " + this.getManagedClass().getCanonicalName() 
			+ " oe WHERE oe.acc = :acc";
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
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
	  if ( src != null )
	  	Validate.notEmpty ( src.getAcc(), "Database access error: cannot fetch an ontology entry with with an empty-accession ref source" );
	  
	  OE oedb = src == null 
	  	? find ( oe.getAcc (), null, null, null ) 
	  	: find ( oe.getAcc (), src.getAcc(), src.getVersion (), src.getUrl () );
	  
	  if ( oedb == null ) {
	    create ( oe );
	    oedb = oe;
	  }
	  return oedb;
	}

	/**
	 * Finds an ontology entry by means of its accession and its source identifier. 
	 * Note that a null version is matched against a null version in the database, the same for the source URL.
	 *  
	 */
	public OE find ( String accession, String srcAcc, String srcVer, String srcUrl ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  //TODO remove Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() + 
			" oe WHERE oe.acc = :acc";
		
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n"
	    	+ " AND " + parameterizedWithNullHql ( "oe.source.version", "srcVer" ) + "\n" 
	    	+ " AND " + parameterizedWithNullHql ( "source.url", "srcUrl" ) + "\n";
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query
			.setParameter ( "srcAcc", srcAcc )
			.setParameter ( "srcVer", srcVer )
			.setParameter ( "srcUrl", srcUrl );
				
		@SuppressWarnings("unchecked")
		List<OE> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
	}
	
	public OE find ( OE example )
	{
		if ( example == null ) return null;
		
		ReferenceSource src = example.getSource ();
		return src == null 
			? find ( example.getAcc (), null, null, null )
		  : find ( example.getAcc (), src.getAcc (), src.getVersion (), src.getUrl () );
	}
	
	
	@SuppressWarnings("unchecked")
	public List<OE> find ( String accession, String srcAcc, String srcVer ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
	  //TODO remove Validate.notEmpty ( srcAcc, "Database access error: cannot fetch an ontology entry with empty accession" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() + 
				" oe WHERE oe.acc = :acc";
			
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n"
	    	+ " AND " + parameterizedWithNullHql ( "oe.source.version", "srcVer" ) + "\n"; 
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query
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
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() 
			+ " oe WHERE oe.acc = :acc";
		
		hql += srcAcc == null
			? " AND oe.source IS NULL"
			: " AND oe.source.acc = :srcAcc\n";
		
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		if ( srcAcc != null ) query
			.setParameter ( "srcAcc", srcAcc );
		
		return query.getResultList();
	}

	@SuppressWarnings ( "unchecked" )
	public List<OE> find ( String accession ) 
	{
	  Validate.notNull ( accession, "Database access error: cannot fetch a null ontology entry" );
		
		String hql = "SELECT oe FROM " + this.getManagedClass().getCanonicalName() 
			+ " oe WHERE oe.acc = :acc";
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession );
		
		return query.getResultList();
	}
	
}
