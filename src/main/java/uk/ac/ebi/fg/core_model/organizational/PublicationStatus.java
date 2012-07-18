package uk.ac.ebi.fg.core_model.organizational;

import javax.persistence.*;

import uk.ac.ebi.fg.core_model.terms.CVTerm;

/**
 * An {@link CVTerm} that describe the status of a publication (eg, submitted, published). We choose to model this as
 * a CV term and not as an ontology entry, because in most applications it is taken from a fixed set of terms, without
 * any reference to the ontology source. Extend {@link Publication} if an OE is needed instead. 
 *
 * @author Marco Brandizi
 * @author Tony Burdett
 */
@Entity
@Table ( name = "publication_status" )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "publication_status_seq" )
public class PublicationStatus extends CVTerm
{
	protected PublicationStatus ()
	{
		super ();
	}

	public PublicationStatus ( String status )
	{
		super ( status );
	}
}
