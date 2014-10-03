package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.xref;

import static uk.ac.ebi.utils.sql.HqlUtils.parameterizedWithNullHql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.Validate;

import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.IdentifiableDAO;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * The DAO to manage {@link ReferenceSource} entities.
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
	 * Tells whether a given source exists, based on its accession, version, url. Note that if the version is null it checks
	 * that there is a record with null version. Allows to select a specific subclass
	 */
	public boolean contains ( String accession, String version, String url, Class<? extends S> targetClass )
	{
		Validate.notEmpty ( accession, "accession must not be empty" );
		
		String hql = "SELECT s.id FROM " + targetClass.getCanonicalName() + 
			" s WHERE s.acc = :acc AND " + parameterizedWithNullHql ( "s.version", "ver" ) + " AND " 
			+ parameterizedWithNullHql ( "s.url", "url" );
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "ver", version )
			.setParameter ( "url", url );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}
	
	/** Wraps {@link #contains(String, String, Class)} with the managed class */ 
	public boolean contains ( String accession, String version, String url ) {
		return contains ( accession, version, url, getManagedClass () ); 
	}
	
	/** Returns any ref source having this accession/version combination, no matter the URL */
	public boolean contains ( String accession, String version, Class<? extends S> targetClass ) 
	{
		Validate.notEmpty ( accession, "accession must not be empty" );
		
		String hql = "SELECT s.id FROM " + targetClass.getCanonicalName() + 
			" s WHERE s.acc = :acc AND " + parameterizedWithNullHql ( "s.version", "ver" ); 
				
		Query query = getEntityManager ().createQuery( hql )
			.setParameter ( "acc", accession )
			.setParameter ( "ver", version );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

	public boolean contains ( String accession, String version ) {
		return contains ( accession, version, getManagedClass () );
	}

	
	/**
	 * Returns true if a record with this accession exists, no matter the version or the URL.
	 * 
	 */
	public boolean contains ( String accession, Class<? extends S> targetClass ) 
	{
		String hql = "SELECT s.id FROM " + targetClass.getCanonicalName() + " s WHERE s.acc = :acc";
				
		Query query = getEntityManager ().createQuery( hql ).setParameter ( "acc", accession );
		
		@SuppressWarnings ( "unchecked" )
		List<Long> list = query.getResultList();
		return !list.isEmpty ();
	}

	/** Wraps {@link #contains(String, Class)} with the managed class */
	public boolean contains ( String accession ) {
		return contains ( accession, getManagedClass () );
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
	  Validate.notEmpty ( src.getAcc(), "Database access error: cannot fetch a reference-source with empty accession" );
	
	  S srcDB = find ( src.getAcc(), src.getVersion (), src.getUrl () );
	  if ( srcDB == null ) 
	  {
	    create ( src );
	    srcDB = src;
	  }
	  return srcDB;
	}

	/**
	 * Finds a source by accession and version. version = null is matched against a record having a null version.
	 * Allows to pick a specific subclass.
	 */
	public S find ( String accession, String version, String url, Class<? extends S> targetClass ) 
	{
	  Validate.notEmpty ( accession, "Database access error: cannot fetch an accessible with empty accession" );
	  
	  String hql = "SELECT s FROM " + targetClass.getCanonicalName() + 
	  	" s WHERE s.acc = :acc AND " + parameterizedWithNullHql ( "s.version", "ver" )
	  	+ " AND " + parameterizedWithNullHql ( "s.url", "url" );
	
	  Query query = getEntityManager ().createQuery ( hql )
	  	.setParameter ( "acc", accession )
	  	.setParameter ( "ver", version )
	  	.setParameter ( "url", url );
		
		@SuppressWarnings("unchecked")
		List<S> result = query.getResultList();
		return result.isEmpty () ? null : result.get ( 0 );
	}
	
	/**
	 * Wraps {@link #find(String, String, Class)} with the managed class.
	 */
	public S find ( String accession, String version, String url ) {
		return find ( accession, version, url, this.getManagedClass () );
	}

	/**
	 * Finds anything with this accession+version, no matter the URL.
	 */
	@SuppressWarnings("unchecked")
	public List<S> find ( String accession, String version, Class<? extends S> targetClass ) 
	{
	  Validate.notEmpty ( accession, "Database access error: cannot fetch an accessible with empty accession" );
	  
	  String hql = "SELECT s FROM " + targetClass.getCanonicalName() + 
	  	" s WHERE s.acc = :acc AND " + parameterizedWithNullHql ( "s.version", "ver" );
	
	  Query query = getEntityManager ().createQuery ( hql )
	  	.setParameter ( "acc", accession )
	  	.setParameter ( "ver", version );

		return query.getResultList();
	} 

	public List<S> find ( String accession, String version ) {
		return find ( accession, version, getManagedClass () );
	} 
	
	
  /**
   * Finds all the versions of a source having a given accession. Allows to pick a specific sub-class. 
   * 
   */
	@SuppressWarnings("unchecked")
  public List<S> find ( String accession, Class<? extends S> targetClass ) 
  {
    Validate.notEmpty ( accession, "Database access error: cannot fetch an empty accessible" );
    
		String queryStr= "SELECT s FROM " + targetClass.getCanonicalName () + " s WHERE s.acc = :acc";
		Query query = getEntityManager ().createQuery ( queryStr );
		query.setParameter ( "acc", accession );
		
		return query.getResultList();
  }

	/**
	 * Wraps {@link #find(String, Class)} with the managed class.
	 */
  public List<S> find ( String accession ) {
	  return find ( accession, this.getManagedClass () );
  }
}
