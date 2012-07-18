package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.DiscriminatorValue;

/**
 * A process consists of producing biomaterials from other biomaterials. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@DiscriminatorValue ( "biomaterial_processing" )
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
