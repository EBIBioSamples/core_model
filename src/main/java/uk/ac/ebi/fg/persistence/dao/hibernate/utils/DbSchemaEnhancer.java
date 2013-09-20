package uk.ac.ebi.fg.persistence.dao.hibernate.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>19 Sep 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DbSchemaEnhancer
{
	public Logger log = LoggerFactory.getLogger ( this.getClass () );
	
	public void enhance ( EntityManagerFactory emf )
	{
		Map<String, Object> props = emf.getProperties (); 
				
		// TODO: Are there more DBs the jerk doesn't work well with?!
		if ( !StringUtils.trimToEmpty ( (String) props.get ( "hibernate.connection.driver_class" ) ).contains ( "Oracle" ) )
			return;
		
		if ( !"create".equals ( (String) props.get ( "hibernate.hbm2ddl.auto" ) ) ) return;
		
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) emf).getSessionFactory ();
		EntityManager em = emf.createEntityManager ();
				
		Set<String> idxSqls = new HashSet<String> ();
		
		for ( ClassMetadata cmeta: sessionFact.getAllClassMetadata ().values () )
		{
			Class<?> eclass = cmeta.getMappedClass ();
			for ( Method meth: eclass.getMethods () )
			{
				for ( Annotation ann: meth.getDeclaredAnnotations () )
				{
					if ( !(ann instanceof JoinTable ) ) continue;
					JoinTable jt = (JoinTable) ann;

					final String tbname = StringUtils.trimToNull ( jt.name () ); 
					if ( tbname == null ) continue;
					
					String ownerJColExpr = "";
					for ( JoinColumn ownerJCol: jt.joinColumns () )
					{
						if ( ownerJColExpr.length () != 0 ) ownerJColExpr += ", ";
						ownerJColExpr += ownerJCol.name ();
					}
										
					final String idxName = tbname + "_owner";
					
					String sqlIdx = String.format ( "CREATE INDEX %s ON %s ( %s )", idxName, tbname, ownerJColExpr );
					if ( idxSqls.contains ( sqlIdx ) ) continue; // Already met, go away!
					idxSqls.add ( sqlIdx );
										
					log.debug ( String.format ( 
						"Fixing Hibernate with indexing command: %s, %s, %s\n", eclass, meth.getName (), sqlIdx ) );

					EntityTransaction ts = em.getTransaction ();
					ts.begin ();
					em.createNativeQuery ( sqlIdx ).executeUpdate ();
					ts.commit ();
				}
			}
		}
	}
}
