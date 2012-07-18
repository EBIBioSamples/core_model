package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;

/**
 * Used to describe a property of a {@link BioMaterial} entity, such as organism's age or cell colture name.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "bio_characteristic_value" )
public class BioCharacteristicValue extends ExperimentalPropertyValue<BioCharacteristicType>
{
	public BioCharacteristicValue () {
		super ();
	}

	public BioCharacteristicValue ( String termText, BioCharacteristicType type ) {
		super ( termText, type );
	}
	
}
