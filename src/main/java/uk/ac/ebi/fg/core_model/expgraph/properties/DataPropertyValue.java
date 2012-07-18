package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.expgraph.Data;

/**
 * Used to describe a property of a {@link Data} entity, such as a file name or a p-value threshold. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "data_property_value" )
public class DataPropertyValue extends ExperimentalPropertyValue<DataPropertyType>
{

	public DataPropertyValue ()
	{
	}

	public DataPropertyValue ( String termText, DataPropertyType type ) {
		super ( termText, type );
	}

}
