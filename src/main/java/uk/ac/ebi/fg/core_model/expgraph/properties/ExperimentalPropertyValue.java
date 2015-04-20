package uk.ac.ebi.fg.core_model.expgraph.properties;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.validator.constraints.NotEmpty;

import uk.ac.ebi.fg.core_model.expgraph.properties.dataitems.DataItem;
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
@Inheritance ( strategy = InheritanceType.SINGLE_TABLE )
@Table ( name = "exp_prop_val" )
@DiscriminatorColumn ( name = "category" )
@DiscriminatorValue ( "generic" )
@DiscriminatorOptions ( force = true )
public class ExperimentalPropertyValue<PT extends ExperimentalPropertyType> extends FreeTextTerm
{
	private PT type;
	private Unit unit;
	private Set<DataItem> dataItems = new HashSet<DataItem> ();

	public ExperimentalPropertyValue () {
		super ();
	}

	public ExperimentalPropertyValue ( String termText, PT type )
	{
		super ( termText );
		this.type = type;
	}

	
	@ManyToOne ( 
		targetEntity = ExperimentalPropertyType.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
		fetch = FetchType.LAZY
	)
	@JoinColumn ( name = "type_id" )	
	public PT getType ()
	{
		return type;
	}

	public void setType ( PT type )
	{
		this.type = type;
	}

	@ManyToOne ( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY )
	@JoinColumn ( name = "unit_id" )
	public Unit getUnit ()
	{
		return unit;
	}

	public void setUnit ( Unit unit )
	{
		this.unit = unit;
	}

	/** Redefines the DB field to be a LOB, since this might be used for things like descriptions */
  @Override
  @Lob @Column ( name = "term_text" ) @NotEmpty
	public String getTermText ()
	{
		return super.getTermText ();
	}
  
  
	@ManyToMany ( targetEntity = DataItem.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
	@JoinTable ( name = "exp_pv_data_item", 
  	joinColumns = @JoinColumn ( name = "pv_id" ), inverseJoinColumns = @JoinColumn ( name = "data_id" ) )
	public Set<DataItem> getDataItems ()
	{
		return dataItems;
	}

	public void setDataItems ( Set<DataItem> dataItems )
	{
		this.dataItems = dataItems;
	}
	
  public boolean addDataItem ( DataItem di ) {
  	return this.getDataItems ().add ( di );
  }

  public boolean removeDataItem ( DataItem di ) {
  	return this.getDataItems ().remove ( di );
  }

  public boolean containsDataItem ( DataItem di ) {
  	return this.getDataItems ().contains ( di );
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
