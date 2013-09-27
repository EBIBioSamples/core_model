package uk.ac.ebi.fg.persistence.hibernate.schema_enhancer;

import javax.persistence.EntityManagerFactory;

/**
 * Performs additions and optimisations after Hibernate initialisation. The current implementations add indices to an
 * Oracle schema that the damn Hibernate doesn't create. This is implemented as a SPI service, see 
 * {@link DbSchemaEnhancerProcessor}, {@link AbstractDbSchemaEnhancer} and {@link DefaultDbSchemaEnhancer} for details.
 *
 * <dl><dt>date</dt><dd>24 Sep 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public interface DbSchemaEnhancer
{

	public void enhance ( EntityManagerFactory emf );

}
