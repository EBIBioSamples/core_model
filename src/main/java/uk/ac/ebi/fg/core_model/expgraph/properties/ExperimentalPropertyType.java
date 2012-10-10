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

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
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
@Table ( name = "exp_property_type" )
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn ( name = "category" )
@DiscriminatorValue ( "generic" )
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
  @Index( name = "p_type_text" )
  @Column ( length = Const.COL_LENGTH_L, name = "term_text" )
	public String getTermText ()
	{
		return super.getTermText ();
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
