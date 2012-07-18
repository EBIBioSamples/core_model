package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Used to qualify a {@link DataPropertyValue}.
 * 
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "data_property_type" )
public class DataPropertyType extends ExperimentalPropertyType
{
	public DataPropertyType ()
	{
		super ();
	}

	public DataPropertyType ( String termText ) {
		super ( termText );
	}

}
