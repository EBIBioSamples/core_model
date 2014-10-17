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
@DiscriminatorValue ( "date" )
public class DateItem extends ValueItem<Date>
{
	protected DateItem () {
		super ();
	}

	public DateItem ( Date value ) {
		super ( value );
	}

	@Column ( name = "date" )
	@Index ( name = "date_item_value" )
	@Override
	public Date getValue ()
	{
		return super.getValue ();
	}

}