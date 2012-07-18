package uk.ac.ebi.fg.core_model.expgraph;

import javax.persistence.DiscriminatorValue;

/**
 * The data processing procedure, which produce data from other data, eg, scan, normalization, scaling, filtering, 
 * gene set enrichment.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@DiscriminatorValue ( "data_processing" )
public class DataProcessing extends Process<Data, Data>
{
	public DataProcessing ()
	{
		super ();
	}

	public DataProcessing ( String acc )
	{
		super ( acc );
	}
}
