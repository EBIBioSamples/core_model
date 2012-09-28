package uk.ac.ebi.fg.core_model.toplevel;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.XRef;


/**
 * An accessible object is any {@link Identifiable} object that also has an assigned accession number. 
 * An accession number is intended as a database-wide unique identifier that can be assigned to objects for external
 * reuse, such as identifiers in the interface, which are distinct from the internal database identifier.
 * Any objects in the model that should be reusable, or otherwise identifiable by their accession number should
 * override this class. However, objects having external accessions, which are assigned by third-parties and are only
 * unique within their contexts (e.g., within an external database), should not be modelled as descendants of this class. 
 * For instance, that is the case of {@link XRef} or {@link OntologyEntry}.  
 * 
 * <dl><dt>Date</dt><dd>Jul 11, 2007</dd></dl>
 *
 * @author Nataliya Sklyar
 * @author mdylags
 * @author Tony Burdett
 * @author Marco Brandizi (migrated to AE2 and revised in 2012)
 */
@MappedSuperclass
public abstract class Accessible extends Identifiable 
{
  private String acc;

  protected Accessible () {
		super ();
	}

	public Accessible ( String acc )
	{
		super ();
		this.acc = acc;
	}

	@Column( unique = true, nullable = false, length = Const.COL_LENGTH_S )
  public String getAcc() {
    return acc;
  }

  protected void setAcc(String acc) {
    this.acc = acc;
  }

  /**
   * If both accessions are non-null, compares them, else uses object identity. 
   */
  @Override
  public boolean equals ( Object o ) 
  {
  	if ( this == o ) return true;
  	if ( o == null ) return false;
  	if ( this.getClass () != o.getClass () ) return false;
  	
    // Compare accessions if both are non-null, use identity otherwise
    Accessible that = (Accessible) o;
    return ( this.getAcc () == null || that.getAcc () == null ) ? false : this.acc.equals ( that.acc );
  }
  
  @Override
  public int hashCode() {
  	return this.getAcc () == null ? super.hashCode () : this.acc.hashCode ();
  }

  @Override
  public String toString() {
    return this.getClass ().getSimpleName () + "{ id: " + getId() + ", acc: '" + getAcc () + "' }";
  }

}
