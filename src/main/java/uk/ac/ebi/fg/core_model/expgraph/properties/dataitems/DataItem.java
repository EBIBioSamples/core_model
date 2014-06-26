package uk.ac.ebi.fg.core_model.expgraph.properties.dataitems;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.toplevel.Annotatable;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>23 Jun 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "data_item" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn ( name = "data_item_class" )
//@DiscriminatorValue ( "generic" ) // TODO: would never be used, not sure Hibernate is fine without any specification
public abstract class DataItem extends Annotatable
{
	protected DataItem () {}
}
