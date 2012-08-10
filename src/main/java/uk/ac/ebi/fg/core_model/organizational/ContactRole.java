package uk.ac.ebi.fg.core_model.organizational;

import javax.persistence.*;

import uk.ac.ebi.fg.core_model.terms.CVTerm;

/**
 * A {@link CVTerm} that describes the role of a contact (eg, submitter, PI, publisher). We choose to model this as
 * a CV term and not as an ontology entry, because in most applications it is taken from a fixed set of terms, without
 * any reference to the ontology source. Extend {@link Contact} if an OE is needed instead. 
 *
 * @author Marco Brandizi
 * @author Tony Burdett
 */
@Entity
@Table ( name = "contact_role" )
public class ContactRole extends CVTerm
{
	protected ContactRole ()
	{
		super ();
	}

	public ContactRole ( String label )
	{
		super ( label );
	}
}
