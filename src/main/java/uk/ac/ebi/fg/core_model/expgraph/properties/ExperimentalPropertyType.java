package uk.ac.ebi.fg.core_model.expgraph.properties;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.terms.FreeTextTerm;

/**
 * An experimental property is used to describe an entity in an experimental workflow. Examples include biomaterial's 
 * characteristics or protocol's parameters. Such properties have a {@link ExperimentalPropertyValue value} and a type
 * (this class), plus an optional {@link Unit} and ontology terms (they extend 
 * {@link FreeTextTerm}).
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "property_type" )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "property_type_seq" )
@DiscriminatorColumn ( name = "category" )
@DiscriminatorValue ( "generic" )
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE )
public class ExperimentalPropertyType extends FreeTextTerm
{
	private int order = 0;
	
	public ExperimentalPropertyType () {
		super ();
	}

	public ExperimentalPropertyType ( String termText ) {
		super ( termText );
	}

	
	
	@Column ( name = "property_order" ) // you cannot define it as 'order', it's an SQL keyword
	public int getOrder ()
	{
		return order;
	}

	public void setOrder ( int order )
	{
		this.order = order;
	}

	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %d, term text: '%s', order: %d, ontology terms:\n  %s\n}", 
			this.getClass ().getSimpleName (), this.getId (), this.getTermText (), this.getOrder (), this.getOntologyTerms () 
		);
	}
}
