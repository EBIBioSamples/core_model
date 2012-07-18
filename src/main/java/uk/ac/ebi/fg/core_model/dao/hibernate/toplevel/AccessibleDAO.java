package uk.ac.ebi.fg.core_model.dao.hibernate.toplevel;

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

	 public boolean existsByAccession ( String accession )
	 {
	    Validate.notNull(accession, "'accession' must not be null");

	    Query query = getEntityManager ().createQuery(
	    	"SELECT a.id FROM " + this.getPersistentClass().getSimpleName() + " a WHERE a.acc = ?1");
	    query.setParameter ( 1, accession );
	    
	    @SuppressWarnings ( "unchecked" )
	    List<Long> list = query.getResultList();
	    return !list.isEmpty ();
	  }

	  public A getOrCreateByAccession ( A entity ) 
	  {

	    Validate.notNull ( entity, "Database access error: cannot fetch an accessible with null accession" );
	    Validate.notEmpty ( entity.getAcc(), "Database access error: cannot fetch an accessible with empty accession" );

	    A accessible = findByAccession ( entity.getAcc() );
	    if ( accessible == null ) 
	    {
	      create ( entity );
	      accessible = entity;
	    }
	    return accessible;
	  }

	  public A findByAccession ( String accession ) 
	  {
	    Validate.notNull ( accession, "Database access error: cannot fetch an accessible with null accession" );
	    Validate.notEmpty ( accession, "Database access error: cannot fetch an accessible with empty accession" );
	    
			String queryStr= "SELECT a FROM " + this.getPersistentClass().getName () + " a WHERE a.acc = ?1";
			Query query = getEntityManager ().createQuery ( queryStr );
			query.setParameter ( 1, accession );
			
			@SuppressWarnings("unchecked")
			List<A> result = query.getResultList();
			return result.isEmpty () ? null : result.get ( 0 );
	  }
	  
}
