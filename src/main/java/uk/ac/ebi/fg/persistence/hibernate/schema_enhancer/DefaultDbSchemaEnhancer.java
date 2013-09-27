package uk.ac.ebi.fg.persistence.hibernate.schema_enhancer;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;



/**
 * Creates those indices that Hibernate doesn't create and which of necessity can be inferred by JPA annotations, e.g., 
 * @@JoinColumn or @@JoinTable. Additionally, creates a set of further indices about the core model via hard-coded calls.  
 *
 * <dl><dt>date</dt><dd>19 Sep 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class DefaultDbSchemaEnhancer extends AbstractDbSchemaEnhancer
{
	public void enhance ( EntityManagerFactory emf )
	{
		super.enhance ( emf );
		
		indexJoinTables ();
		indexManyToOnes ();
		indexDiscriminatorCols ();		
		
		// More join tables, that cannot easily be achieved from annotations
		Set<String> alreadyDone = new HashSet<String> ();
				
		indexJoinTable ( "contact_annotation", "owner_id", "annotation_id", alreadyDone );
		indexJoinTable ( "organization_annotation", "owner_id", "annotation_id", alreadyDone );
		indexJoinTable ( "publication_annotation", "owner_id", "annotation_id", alreadyDone );

		indexJoinTable ( "bio_product_annotation", "owner_id", "annotation_id", alreadyDone );
		indexJoinTable ( "process_annotation", "owner_id", "annotation_id", alreadyDone );
		indexJoinTable ( "protocol_annotation", "owner_id", "annotation_id", alreadyDone );
		
		indexJoinTable ( "exp_prop_type_onto_entry", "owner_id", "oe_id", alreadyDone );
		indexJoinTable ( "exp_prop_val_onto_entry", "owner_id", "oe_id", alreadyDone );
		indexJoinTable ( "unit_onto_entry", "owner_id", "oe_id", alreadyDone );
		indexJoinTable ( "unit_dim_onto_entry", "owner_id", "oe_id", alreadyDone );
		
		index ( "papp_proc", "protocol_app", "process_id", alreadyDone );
	}
}
