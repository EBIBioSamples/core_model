package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.internal.xml.PropertyType;

import uk.ac.ebi.fg.core_model.expgraph.Protocol;
import uk.ac.ebi.fg.core_model.expgraph.ProtocolApplication;

/**
 * Used to qualify a {@link ParameterValue}. {@link Protocol}s have parameter types (i.e., uninstantiated variable names), 
 * which are instantiated by {@link ProtocolApplication} via the {@link ProtocolApplication#getParameterValues() parameterValues}
 * relation, the {@link ParameterValue}s there should be attached to {@link PropertyType}s.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "parameter_type" )
public class ParameterType extends ExperimentalPropertyType
{
	public ParameterType () {
		super ();
	}

	public ParameterType ( String termText ) {
		super ( termText );
	}

}
