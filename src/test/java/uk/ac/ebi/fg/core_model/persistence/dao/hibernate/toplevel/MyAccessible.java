/*
 * 
 */
package uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.toplevel.Accessible;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "test_accessible" )
public class MyAccessible extends Accessible
{
	private String propOne, propTwo;
	
	protected MyAccessible () {
		super ();
	}
	
	public MyAccessible ( String acc ) {
		super ( acc );
	}

	@Column ( name = "one", nullable = true )
	public String getPropOne ()
	{
		return propOne;
	}

	public void setPropOne ( String propOne )
	{
		this.propOne = propOne;
	}

	@Column ( name = "two", nullable = true )
	public String getPropTwo ()
	{
		return propTwo;
	}

	public void setPropTwo ( String propTwo )
	{
		this.propTwo = propTwo;
	}
	
}
