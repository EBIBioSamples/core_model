package uk.ac.ebi.fg.core_model.expgraph.properties.dataitems;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Index;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>23 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@DiscriminatorValue ( "number" )
public class NumberItem extends ValueItem<Double>
{

	protected NumberItem () {
		super ();
	}

	public NumberItem ( Double value ) {
		super ( value );
	}

	
	@Column ( name = "number_val" )
	@Index ( name = "number_item_value" )
	@Override
	public Double getValue ()
	{
		return super.getValue ();
	}
}
