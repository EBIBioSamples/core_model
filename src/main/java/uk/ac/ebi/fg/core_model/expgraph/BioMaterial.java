package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.expgraph.properties.ExperimentalPropertyValue;

/**
 * 
 * <p>A biological material, such as a blood sample, a cell culture, a mouse, a group of patients, a biological extract.
 * This class may have specialisations like bio-source or sample in specific extensions of the core model. Note that
 * the core model aim at representing those materials that have a direct role in the experiment, i.e., are the entities 
 * that are studied. For instance, reagents should not fall under this category.</p>
 * 
 * <p>This class is kept with the EP generic, since there are extensions that need to store heterogeneous properties 
 * (e.g., bio-characteristics + non-bio properties like comments or provider).</p>
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "generic_bio_material" )
@SuppressWarnings ( "rawtypes" )
public class BioMaterial<EP extends ExperimentalPropertyValue> extends Product<EP>
{
	public BioMaterial ()
	{
		super ();
	}

	public BioMaterial ( String acc )
	{
		super ( acc );
	}
}
