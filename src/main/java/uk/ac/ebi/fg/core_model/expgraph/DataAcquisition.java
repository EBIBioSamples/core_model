package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The process that allows one to acquire some data from biomaterials. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "data_acquisition" )
@SuppressWarnings ( "rawtypes" )
public class DataAcquisition extends Process<BioMaterial, Data>
{
	public DataAcquisition ()
	{
		super ();
	}

	public DataAcquisition ( String acc )
	{
		super ( acc );
	}
}
