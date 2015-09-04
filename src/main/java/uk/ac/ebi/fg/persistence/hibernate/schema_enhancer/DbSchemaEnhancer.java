package uk.ac.ebi.fg.persistence.hibernate.schema_enhancer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Contains utility functions for performing Oracle schema enhancement and a default invocation of 
 * {@link #enhance(EntityManagerFactory)}. If your model extends the hereby core model, you'll probably want to extend 
 * this class and add your specific operations.
 * See the <a href = 'https://github.com/EBIBioSamples/biosd_model'>biosd_model</a> for details. 
 *
 * <dl><dt>date</dt><dd>19 Sep 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public abstract class DbSchemaEnhancer
{
	protected Logger log = LoggerFactory.getLogger ( this.getClass () );
	
	protected EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;

	public void enhance ( EntityManagerFactory emf )
	{
		this.entityManagerFactory = emf;
		entityManager = entityManagerFactory.createEntityManager ();
	}

	/**
	 * Lookup all the {@link JoinTable} annotations in the model model that is accessible from the classpath (i.e., as 
	 * defined in persistence.xml) and, if these defines the table name and both the join column and the inverse
	 * join column, it creates one index for each of such columns. This is necessary because the bloody 
	 * <a href = 'https://hibernate.atlassian.net/browse/HHH-4263'>Hibernate doesn't do it automatically</a>.
	 */
	protected void indexJoinTables () 
	{		
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory ();
				
		Set<String> idxDefs = new HashSet<String> ();
		
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
					
					String fkExpr = "";
					for ( JoinColumn fkCol: jt.inverseJoinColumns () )
					{
						if ( fkExpr.length () != 0 ) fkExpr += ", ";
						fkExpr += fkCol.name ();
					}
					
					if ( ownerJColExpr.length () == 0 || fkExpr.length () == 0 ) continue;
															
					indexJoinTable ( tbname, ownerJColExpr, fkExpr, idxDefs );					
				} // for ann
			} // for meth
		} // for cmeta
		
	} // indexJoinTables

	/**
	 * Finds all @ManyToOne annotations in available entity classes and, if @Table and {@link JoinColumn} are defined too,
	 * create and index on the joining column, which Hibernate doesn't usually create.
	 * 
	 */
	protected void indexManyToOnes () 
	{		
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory ();
				
		Set<String> idxDefs = new HashSet<String> ();
		
		for ( ClassMetadata cmeta: sessionFact.getAllClassMetadata ().values () )
		{
			Class<?> eclass = cmeta.getMappedClass ();
			Table tba = eclass.getAnnotation ( Table.class );
			if ( tba == null ) continue;
			
			final String tbname = StringUtils.trimToNull ( tba.name () );
			if ( tbname == null ) continue;

			ManyToOne m2o = null;
			JoinColumn fkCol = null;
			
			for ( Method meth: eclass.getMethods () )
			{
				for ( Annotation ann: meth.getDeclaredAnnotations () )
				{
					if ( ann instanceof ManyToOne ) m2o = (ManyToOne) ann;
					else if ( ann instanceof JoinColumn ) fkCol = (JoinColumn) ann;
				} // for ann
				
				if ( m2o == null || fkCol == null ) continue;
				
				String fkColName = StringUtils.trimToNull ( fkCol.name () );
				if ( fkColName == null ) continue;
				
				index ( tbname, fkColName, idxDefs );
			} // for meth
		} // for cmeta
		
	} // indexJoinTables	

	/**
	 * Finds all entity classes having both {@link Table} and {@link DiscriminatorColumn} annotations and create 
	 * indices for the discriminator columns, which Hibernate doesn't seem to do.
	 */
	protected void indexDiscriminatorCols ()
	{
		SessionFactory sessionFact = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory ();
				
		Set<String> idxDefs = new HashSet<String> ();
		
		for ( ClassMetadata cmeta: sessionFact.getAllClassMetadata ().values () )
		{
			Class<?> eclass = cmeta.getMappedClass ();
			
			Table tba = eclass.getAnnotation ( Table.class );
			if ( tba == null ) continue;
			
			DiscriminatorColumn discrAnn = eclass.getAnnotation ( DiscriminatorColumn.class );
			if ( discrAnn == null ) continue;
			String discrCol = StringUtils.trimToNull ( discrAnn.name () );
			if ( discrCol == null ) continue;
			
			final String tbname = StringUtils.trimToNull ( tba.name () );
			if ( tbname == null ) continue;

			index ( tbname + "_discr", tbname, discrCol, idxDefs );
		}
	}
	
	/**
	 * Creates one index per column, naming them automatically. alreadyDone is passed to 
	 * {@link #index(String, String, String, Set)}.
	 */
	protected void indexJoinTable ( String tbname, String ownerCol, String fkCol, Set<String> alreadyDone )
	{
		index ( tbname + "_own", tbname, ownerCol, alreadyDone );
		index ( tbname + "_fk", tbname, fkCol, alreadyDone );
	}

	/**
	 * Creates and index. Skips it if the SQL used to do so is already in the alreadyDone parameter, or if an index 
	 * with the same name already exists, or if the column to index is already in some other index.
	 * 
	 * If indexName is null, it creates an index name automatically, joining tableName and colName.
	 * 
	 */
	protected void index ( String indexName, String tableName, String colName, Set<String> alreadyDone ) 
	{
		if ( indexName == null ) indexName = tableName + "_" + colName;
		String sql = String.format ( "CREATE INDEX %s ON %s ( %s )", indexName, tableName, colName );

		if ( alreadyDone.contains ( sql ) ) return;
		
		// is this index already here?
		long idxCt = ((BigDecimal) entityManager.createNativeQuery ( 
			"SELECT COUNT(*) FROM user_indexes WHERE UPPER ( index_name ) = UPPER ( :idxName )" )
		  .setParameter ( "idxName", indexName )
		  .getSingleResult ()).longValue ();
		if ( idxCt > 0 )  { alreadyDone.add ( sql ); return; }
		
		// Is this column already indexed?
		@SuppressWarnings ( "unchecked" )
		List<BigDecimal> idxColCt = entityManager.createNativeQuery ( 
			"SELECT COUNT ( DISTINCT o.column_name ) AS col_ct\n" +  
			"FROM user_ind_columns o, user_ind_columns i\n" +
			"WHERE UPPER ( i.table_name ) = UPPER ( :tbName ) AND UPPER ( i.column_name ) = UPPER ( :colName )\n" +
			"AND o.index_name = i.index_name\n" +
			"GROUP BY o.index_name ORDER BY col_ct ASC"
			)
			.setParameter ( "tbName", tableName )
		  .setParameter ( "colName", colName )
		  .getResultList ();
		if ( idxColCt != null && !idxColCt.isEmpty () && idxColCt.iterator ().next ().longValue () == 1 ) { 
			alreadyDone.add ( sql ); return; 
		}
		
		wrapSqlUpdate ( sql, alreadyDone );
	}

	/**
	 * A wrapper of {@link #index(String, String, String, Set)} with tableName == null.
	 * 
	 */
	protected void index ( String tableName, String column, Set<String> alreadyDone ) {
		index ( null, tableName, column, alreadyDone );
	}

	/**
	 * Wraps some DDL command into a transaction and exception management. If the command fails, it logs the problem, but 
	 * doesn't allow the Java application to fail too. Moreover, if sql is already in alreadyDone, it doesn't run 
	 * it twice. 
	 */
	protected void wrapSqlUpdate ( String sql, Set<String> alreadyDone ) 
	{
		if ( alreadyDone.contains ( sql ) ) return;
		alreadyDone.add ( sql );
		
		EntityTransaction ts = entityManager.getTransaction ();
		try {
			ts.begin ();
			entityManager.createNativeQuery ( sql ).executeUpdate ();
			ts.commit ();
		} 
		catch ( PersistenceException ex ) 
		{
			if ( ts.isActive () ) ts.rollback ();
						
			log.warn ( "SQL exception while running '{}', statement will be ignored, likely it's OK to do so", sql );
			log.debug ( String.format ( "Statement: '%s', but you could still be fine", sql ), ex );
		}
	}
}
