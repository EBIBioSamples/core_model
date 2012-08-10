/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.toplevel;

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
	protected MyAccessible () {
		super ();
	}
	
	public MyAccessible ( String acc ) {
		super ( acc );
	}
}
