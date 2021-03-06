package uk.ac.ebi.fg.core_model.expgraph;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.expgraph.properties.ParameterType;
import uk.ac.ebi.fg.core_model.expgraph.properties.ParameterValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.ProtocolType;
import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;


/**
 * 
 * The representation of a laboratory protocol. This is the plan associated to the protocol and not one of its particular
 * executions, for which {@link ProtocolApplication} is used. A protocol can have a list of 
 * {@link #getParameterTypes() parameter types} associated, which are instantiated with {@link ParameterValue values}
 * in {@link ProtocolApplication}.
 * 
 *
 * <dl><dt>date</dt><dd>Jul 5, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table( name = "protocol" )
public class Protocol extends Accessible 
{
  private String name;
  private String title;
  private String description;
  private String hardware;
  private String software;
  private String contact;
  private String uri;
  
  private ProtocolType type;
  private Collection<ParameterType> parameterTypes = new ArrayList<ParameterType>();

  @Column ( length = Const.COL_LENGTH_M )
  @Index( name = "proto_name" )
	public String getName ()
	{
		return name;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

  @Column ( length = Const.COL_LENGTH_L )
  @Index( name = "proto_title" )
	public String getTitle ()
	{
		return title;
	}

	public void setTitle ( String title )
	{
		this.title = title;
	}

	@Lob
	public String getDescription ()
	{
		return description;
	}

	public void setDescription ( String description )
	{
		this.description = description;
	}

  @Column ( length = Const.COL_LENGTH_XL )
	@Index ( name = "proto_hw" )
	public String getHardware ()
	{
		return hardware;
	}

	public void setHardware ( String hardware )
	{
		this.hardware = hardware;
	}

  @Column ( length = Const.COL_LENGTH_XL )
	@Index ( name = "proto_sw" )
	public String getSoftware ()
	{
		return software;
	}

	public void setSoftware ( String software )
	{
		this.software = software;
	}

  @Column ( length = Const.COL_LENGTH_XL )
	@Index ( name = "proto_cnt" )
	public String getContact ()
	{
		return contact;
	}

	public void setContact ( String contact )
	{
		this.contact = contact;
	}

  @Column ( length = 255 )
	@Index ( name = "proto_uri" )
	public String getUri ()
	{
		return uri;
	}

	public void setUri ( String uri )
	{
		this.uri = uri;
	}

  @ManyToOne( cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
  @JoinColumn ( name = "type_id" )
	public ProtocolType getType ()
	{
		return type;
	}

	public void setType ( ProtocolType type )
	{
		this.type = type;
	}

  @OneToMany( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval=true )
  @JoinTable( name = "protocol_parameter_type", 
  	joinColumns = @JoinColumn ( name = "protocol_id" ), inverseJoinColumns = @JoinColumn ( name = "param_type_id" )
  )
	public Collection<ParameterType> getParameterTypes ()
	{
		return parameterTypes;
	}

	public void setParameterTypes ( Collection<ParameterType> parameterTypes )
	{
		this.parameterTypes = parameterTypes;
	}
	

	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %s acc: '%s', name: '%s', title: '%s', type: '%s', description:\n  '%.40s'\n, uri: '%s', contact: '%s', " +
			"hardware: '%s', software: '%s'}", 
			this.getClass ().getSimpleName (), this.getId (), this.getAcc (), this.getName (), this.getTitle (),
			this.getType (), this.getDescription (), this.getUri (), this.getContact (), this.getHardware (), 
			this.getSoftware ()
		);
	}
  
	
}
