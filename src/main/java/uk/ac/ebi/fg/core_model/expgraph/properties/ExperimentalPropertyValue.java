package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.terms.FreeTextTerm;

/**
 * 
 * An experimental property is used to describe an entity in an experimental workflow. Examples include biomaterial's 
 * characteristics or protocol's parameters. Such properties have a value (this class) and a 
 * {@link ExperimentalPropertyType type}, plus an optional {@link Unit} and ontology terms (they extend 
 * {@link FreeTextTerm}).
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "property_value" )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "property_value_seq" )
@DiscriminatorColumn ( name = "category" )
@DiscriminatorValue ( "generic" )
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE )
public class ExperimentalPropertyValue<PT extends ExperimentalPropertyType> extends FreeTextTerm
{
	@ManyToOne ( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
	@Column ( name = "type_id" )
	private PT type;

	@ManyToOne ( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
	@Column ( name = "unit_id" )
	private Unit unit;

	public ExperimentalPropertyValue () {
		super ();
	}

	public ExperimentalPropertyValue ( String termText, PT type )
	{
		super ( termText );
		this.type = type;
	}

	
	public PT getType ()
	{
		return type;
	}

	public void setType ( PT type )
	{
		this.type = type;
	}

	public Unit getUnit ()
	{
		return unit;
	}

	public void setUnit ( Unit unit )
	{
		this.unit = unit;
	}
	
	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %d, value: '%s', type: %s, unit:\n  %s,\n  ontology terms:\n  %s\n}", 
			this.getClass ().getSimpleName (), this.getId (), this.getTermText (), this.getType (), this.getUnit (), this.getOntologyTerms () 
		);
	}

}
