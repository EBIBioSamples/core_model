package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.terms.FreeTextTerm;

/**
 * The measurement unit associated to the (numerical) value reported by an {@link ExperimentalPropertyValue}. A unit 
 * should be associated to the type (i.e., {@link UnitDimension dimension}) it measures.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "unit" )
public class Unit extends FreeTextTerm
{
	private UnitDimension dimension;
	
	public Unit () {
		super ();
	}

	public Unit ( String termText, UnitDimension dimension )
	{
		super ( termText );
		this.dimension = dimension;
	}

	@ManyToOne( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	@JoinColumn( name = "dimension_id", nullable = true)
	public UnitDimension getDimension ()
	{
		return dimension;
	}

	public void setDimension ( UnitDimension dimension )
	{
		this.dimension = dimension;
	}
	
	@Override
  @Index( name = "unit_text" )
  @Column ( length = Const.COL_LENGTH_L, name = "term_text" )
	public String getTermText ()
	{
		return super.getTermText ();
	}
	
	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %d, value: '%s',\n  dimension: %s,\n  ontology terms: %s\n}", 
			this.getClass ().getSimpleName (), this.getId (), this.getTermText (), this.getDimension (), this.getOntologyTerms () 
		);
	}
	
}
