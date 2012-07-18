package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import uk.ac.ebi.fg.core_model.expgraph.Protocol;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * A term to characterise what type a {@link Protocol} is, eg, treatment, scan, normalization. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "protocol_type" )
public class ProtocolType extends OntologyEntry
{
	public ProtocolType () {
	}

	public ProtocolType ( String acc, ReferenceSource source ) {
		super ( acc, source );
	}
}
