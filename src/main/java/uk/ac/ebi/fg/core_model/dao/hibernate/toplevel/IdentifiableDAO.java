package uk.ac.ebi.fg.core_model.dao.hibernate.toplevel;

import javax.persistence.EntityManager;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.perfectjpattern.jee.api.integration.dao.DaoException;
import org.perfectjpattern.jee.integration.dao.AbstractHibernateManagedGenericDao;

import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * The interface between the core model and the relational database it is mapped to is managed via the Data Access Object 
 * design pattern. This is a base DAO, from which all the others are derived. All DAOs in this package manages the storage
 * via Hibernate and JPA annotations.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class IdentifiableDAO<D extends Identifiable> extends AbstractHibernateManagedGenericDao<Long, D>
{
	private EntityManager entityManager; 
	
	public IdentifiableDAO ( Class<D> managedClass, EntityManager entityManager ) 
	{
		super ( managedClass );
		this.setEntityManager ( entityManager );
	}

	public EntityManager getEntityManager ()
	{
		return entityManager;
	}

	public void setEntityManager ( EntityManager entityManager )
	{
		super.setEntityManager ( entityManager );
		this.entityManager = entityManager;
	}

	@Override
	public int count () throws DaoException
	{
		throw new UnsupportedOperationException ( 
			"Internal error: count() is not supported cause int is not enough for us, use countAsLong() instead" );
	}
	
	public long countAsLong () throws DaoException
	{
		Criteria criteria = super.getActualSession ().createCriteria ( getPersistentClass() ).
		setProjection ( Projections.rowCount() ).setCacheMode ( CacheMode.IGNORE );
		
		//JPA 2.0
		Long value = (Long) criteria.list().get( 0 );
		return value;
	}

	@Override
	public Session getActualSession () throws DaoException {
		return super.getActualSession ();
	}
	
}
