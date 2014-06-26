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
@DiscriminatorValue ( "number_range" )
public class NumberRangeItem extends RangeItem<Double>
{
	protected NumberRangeItem () {
		super ();
	}

	public NumberRangeItem ( Double low, Double hi ) {
		super ( low, hi );
	}

	
	@Column ( name = "number_low" )
	@Index ( name = "number_range_lo" )
	@Override
	public Double getLow ()
	{
		return super.getLow ();
	}

	@Column ( name = "number_hi" )
	@Index ( name = "number_range_hi" )
	@Override
	public Double getHi ()
	{
		return super.getHi ();
	}

}
