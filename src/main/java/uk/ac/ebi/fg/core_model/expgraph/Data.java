package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.expgraph.properties.ExperimentalPropertyValue;

/**
 * <p>A data entity, such as a .CEL file, a data matrix of gene expression levels, a protein peak file. 
 * Specific implementations of this core model can extend this class with subclasses like raw data or normalised data.</p>
 *  
 * <p>Note that this intended mostly as the output of an experiment and not something orthogonal (which should be 
 * modelled elsewhere, or by means of generic models, such as annotations). For example, a parameter setting file is 
 * more a protocol parameter value than an instance of this class.</p> 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Table( name = "generic_data" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn ( name = "data_type_class" )
@DiscriminatorValue ( "generic" )
@AssociationOverrides({ 
	@AssociationOverride ( name = "upstreamProcesses", joinTable = @JoinTable ( name = "process_data_output" ) ), 
	@AssociationOverride ( name = "downstreamProcesses", joinTable = @JoinTable ( name = "process_data_input" ) ),
	@AssociationOverride ( name = "annotations", joinTable = @JoinTable( name = "data_annotation" ) )
})
public class Data<EP extends ExperimentalPropertyValue> extends Product<EP>
{
	public Data ()
	{
		super ();
	}

	public Data ( String acc )
	{
		super ( acc );
	}
}
