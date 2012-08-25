package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A process consists of producing biomaterials from other biomaterials. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "biomaterial_processing" )
@SuppressWarnings ( "rawtypes" )
public class BioMaterialProcessing extends Process<BioMaterial, BioMaterial>
{

	public BioMaterialProcessing ()
	{
		super ();
	}

	public BioMaterialProcessing ( String acc )
	{
		super ( acc );
	}
	
}
