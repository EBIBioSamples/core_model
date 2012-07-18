package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.terms.FreeTextTerm;

/**
 * The unit type (dimension) a {@link Unit} is about, e.g., temperature, mass, length. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "unit_dimension" )
public class UnitDimension extends FreeTextTerm
{
	public UnitDimension () {
		super ();
	}

	public UnitDimension ( String termText ) {
		super ( termText );
	}
	
}
