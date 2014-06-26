package uk.ac.ebi.fg.core_model.expgraph.properties.dataitems;

import java.util.Date;

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
@DiscriminatorValue ( "date_range" )
public class DateRangeItem extends RangeItem<Date>
{
	protected DateRangeItem () {
		super ();
	}

	public DateRangeItem ( Date low, Date hi ) {
		super ( low, hi );
	}

	
	
	@Column ( name = "date_low" )
	@Index ( name = "date_range_lo" )
	@Override
	public Date getLow ()
	{
		return super.getLow ();
	}

	@Column ( name = "date_hi" )
	@Index ( name = "date_range_hi" )
	@Override
	public Date getHi ()
	{
		return super.getHi ();
	}
}
