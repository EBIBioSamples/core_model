package uk.ac.ebi.fg.core_model.expgraph;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;

import uk.ac.ebi.fg.core_model.expgraph.properties.ParameterValue;
import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * A {@link Process} may consist of the execution of a sequence of protocols, this class is to store such sequence and, for
 * every protocol that is being employed, the collection of {@link ParameterValue}s used for that particular protocol 
 * application. The parameter values should correspond to the types in the {@link Protocol} referred by the protocol
 * application. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Embeddable
@SequenceGenerator ( name = "hibernate_seq" )
public class ProtocolApplication extends Identifiable
{
	private Protocol protocol;
	private Collection<ParameterValue> parameterValues;
	private int order;
	
	protected ProtocolApplication () {
		super ();
	}

	public ProtocolApplication ( Protocol protocol ) {
		this.protocol = protocol;
	}

	public Protocol getProtocol ()
	{
		return protocol;
	}

	public void setProtocol ( Protocol protocol )
	{
		this.protocol = protocol;
	}

	public Collection<ParameterValue> getParameterValues ()
	{
		return parameterValues;
	}

	public void setParameterValues ( Collection<ParameterValue> parameterValues )
	{
		this.parameterValues = parameterValues;
	}
	
	public boolean addParameterValue ( ParameterValue pval ) {
		return this.parameterValues.add ( pval );
	}

	@JoinColumn ( name = "application_order" )
	public int getOrder ()
	{
		return order;
	}

	@Column ( name = "protocol_order" ) // You need this change, cause order is an SQL keyword
	public void setOrder ( int order )
	{
		this.order = order;
	}
	

	@Override
  public String toString() 
  {
  	return String.format ( 
  		"type: %s { id: %d, acc: %s, order: %d, protocol: %s, parameters:\n  %s\n}", 
  		this.getClass ().getSimpleName (), this.getId (), this.getOrder (), 
  		this.getProtocol () == null ? null : this.protocol.getAcc () + " (" + this.protocol.getName () + " )",
  		this.getParameterValues ()
  	);
  }
	
}
