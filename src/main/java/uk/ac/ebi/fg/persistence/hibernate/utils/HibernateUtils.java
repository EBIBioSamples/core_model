package uk.ac.ebi.fg.persistence.hibernate.utils;

import java.util.Collections;

import javax.persistence.EntityManager;

import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;


/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>27 Jan 2015</dd>
 *
 */
public class HibernateUtils
{
	/**
	 * Translates an HQL query to SQL, based on current Hibernate configuration passed in the em, ie, using the 
	 * Hibernate dialect. Many thanks to the contributor of 
	 * <a href = 'http://stackoverflow.com/questions/11739929/hql-to-sql-in-java-5'>this StackOverflow question</a>.
	 * 
	 * @param isShallowSelect should be true, if it's a query to select a scalar or an entity ID, this is passed to
	 * {@link QueryTranslator#compile(java.util.Map, boolean)}
	 */
	public static String hql2sql ( String hql, boolean isShallowSelect, EntityManager em )
	{
		SessionFactoryImplementor sf = ((EntityManagerFactoryImpl) em.getEntityManagerFactory ()).getSessionFactory();
		QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();			
		QueryTranslator translator = translatorFactory.createQueryTranslator ( hql, hql, Collections.emptyMap (), sf );
		translator.compile ( Collections.emptyMap (), isShallowSelect );
		return translator.getSQLString (); 
	}
}
