package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.expgraph.Protocol;
import uk.ac.ebi.fg.core_model.expgraph.ProtocolApplication;

/**
 * The particular value for a {@link Protocol#getParameterTypes() protocol parameter} that is used in a {@link ProtocolApplication}.  
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "parameter_value" )
public class ParameterValue extends ExperimentalPropertyValue<ParameterType>
{
	public ParameterValue () {
		super ();
	}

	public ParameterValue ( String termText, ParameterType type ) {
		super ( termText, type );
	}

}
