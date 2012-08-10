package uk.ac.ebi.fg.core_model.expgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.expgraph.Node;
import uk.ac.ebi.fg.core_model.toplevel.Annotation;
import uk.ac.ebi.fg.core_model.toplevel.DefaultAnnotatable;

/**
 * 
 * <p>A process represent the activity that lead from an experimental {@link Product} to another. For instance, the treatment
 * of some biomaterial, the measurement of some property and achievement of some data, some data transformation, such as
 * normalization. A {@link ProtocolApplication sequence of protocols} can be associated to a process, to detail which
 * experimental lab protocols the process has consisted of. In the core model convenience specific processes are provided
 * with ({@link BioMaterialProcessing}, {@link DataAcquisition}, {@link DataProcessing}), mainly to restrict the type 
 * of inputs and outputs that one can expect at a given experimental stage. This specific processes are.</p>   
 * 
 * <p>I and O are the type of inputs and outputs that the process accepts. These are bound to suitable product types in specific
 * implementations. We don't specify any accepted process type on the {@link Product} side, cause it is usually more important
 * to make restrictions on process types.</p> 
 *
 * <dl><dt>date</dt><dd>Jun 29, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Table ( name = "process" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn ( name = "process_type_class" )
@DiscriminatorValue ( "generic" )
public class Process<I extends Product, O extends Product> extends Node<I, O>
{
	@OneToMany
	private List<ProtocolApplication> protocolApplications = new ArrayList<ProtocolApplication> ();
	
	public Process ()
	{
		super ();
	}

	public Process ( String acc )
	{
		super ( acc );
	}

	/** 
	 * This is implemented via {@link #getUpstreamNodes()}, i.e., the upstream nodes of a process are its inputs. See
	 * {@link Product} for details. 
	 *  
	 */
	@ManyToMany ( mappedBy = "downstreamProcesses", cascade = CascadeType.ALL )
	public Set<I> getInputs ()
	{
		return this.getUpstreamNodes ();
	}

	/** 
	 * This is implemented via {@link #getUpstreamNodes()}, i.e., the upstream nodes of a process are its inputs. See
	 * {@link Product} for details. 
	 *  
	 */
	protected void setInputs ( Set<I> inputs )
	{
		this.setUpstreamNodes ( inputs );
	}

	/** 
	 * This is implemented via {@link #getUpstreamNodes()}, i.e., the upstream nodes of a process are its inputs. 
	 * This also means changes are coordinated with the symmetric side of this relation (ie, addition/removal of an input 
	 * produces addition/removal of 'this' to/from the input's downstream processes). See {@link Product} for details. 
	 *  
	 */
	public boolean addInput ( I input )
	{
		return this.addUpstreamNode ( input );
	}
	
	/** 
	 * This is implemented via {@link #getUpstreamNodes()}, i.e., the upstream nodes of a process are its inputs. 
	 * This also means changes are coordinated with the symmetric side of this relation (i.e., addition/removal of an input 
	 * produces addition/removal of 'this' to/from the input's downstream processes). See {@link Product} for details.
	 * 
	 *  
	 */
	public boolean removeInput ( I input )
	{
		return this.removeUpstreamNode ( input );
	}

	
	/** 
	 * This is implemented via {@link #getDownstreamNodes()}, i.e., the down-stream nodes of a process are its inputs.
	 * See {@link Product} for details.
	 */
	@ManyToMany ( mappedBy = "upstreamProcesses", cascade = CascadeType.ALL )
	public Set<O> getOutputs ()
	{
		return this.getDownstreamNodes ();
	}

	/** 
	 * This is implemented via {@link #getDownstreamNodes()}, i.e., the down-stream nodes of a process are its inputs.
	 */
	protected void setOutputs ( Set<O> outputs )
	{
		this.setOutputs ( outputs );
	}
	
	/** 
	 * This is implemented via {@link #getDownstreamNodes()}, i.e., the down-stream nodes of a process are its inputs. 
	 * This also means changes are coordinated with the symmetric side of this relation (ie, addition/removal of an output 
	 * produces addition/removal of 'this' to/from the output's upstream processes).
	 */
	public boolean addOutput ( O out )
	{
		return this.addDownstreamNode ( out );
	}
	
	/** 
	 * This is implemented via {@link #getDownstreamNodes()}, i.e., the down-stream nodes of a process are its inputs.
	 * This also means changes are coordinated with the symmetric side of this relation (ie, addition/removal of an output 
	 * produces addition/removal of 'this' to/from the output's upstream processes).
	 */
	public boolean removeOutput ( O out )
	{
		return this.removeDownstreamNode ( out );
	}
	
	public List<ProtocolApplication> getProtocolApplications ()
	{
		return protocolApplications;
	}

	public void setProtocolApplications ( List<ProtocolApplication> protocolApplications )
	{
		this.protocolApplications = protocolApplications;
	}
	
	public boolean addProtocolApplication ( ProtocolApplication papp ) {
		return this.protocolApplications.add ( papp );
	}
	
  @Override
  public String toString() 
  {
  	return String.format ( 
  		"%s { id: %d, acc: %s, protocols:\n  %s\n}", 
  		this.getClass ().getSimpleName (), this.getId (), this.getAcc (), this.getProtocolApplications () 
  	);
  }
}
