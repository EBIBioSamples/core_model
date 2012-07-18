package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

@Entity
@DiscriminatorValue ( "data_type" )
public class DataType extends OntologyEntry
{
	public DataType () {
	}

	public DataType ( String acc, ReferenceSource source ) {
		super ( acc, source );
	}
}
