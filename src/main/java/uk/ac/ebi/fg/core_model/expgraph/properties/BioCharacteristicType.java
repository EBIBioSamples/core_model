package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Used to qualify the type of a {@link BioCharacteristicValue}. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "bio_characteristic_type" )
public class BioCharacteristicType extends ExperimentalPropertyType
{
	public BioCharacteristicType () {
		super ();
	}

	public BioCharacteristicType ( String termText ) {
		super ( termText );
	}
	
}
