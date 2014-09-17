package uk.ac.ebi.fg.core_model.terms;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.NotEmpty;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.Identifiable;


/**
 * A controlled vocabulary term. This is used for those terms that are used in a particular context 
 * (e.g., an application, an organisation) and as such don't need further specification (in particular, they don't
 * need to specify a vocabulary). Because of that, a CVTerm is simply an entity with a name. 
 * 
 * <dl><dt>Date</dt><dd>May, 2012</dd></dl>
 * 
 * @author  mdylag
 * @author Marco Brandizi (migrated to AE2's ControlledVocabulary and revised in 2012)
 * 
 */
@MappedSuperclass
public abstract class CVTerm extends Identifiable 
{
	private String name;

	protected CVTerm () {
		super ();
	}
	
	public CVTerm ( String name ) {
		super ();
		this.name = name;
	}

	@NotEmpty
	@Column( unique = true, nullable = false, length = Const.COL_LENGTH_L )
	public String getName() {
		return name;
	}

  protected void setName ( String name ) {
    this.name = name;
  }

  /**
   * If both names are non-null, compares them, else uses {@link Identifiable} criterion, i.e., object identity. 
   */
  @Override
  public boolean equals ( Object o ) 
  {
  	if ( this == o ) return true;
  	if ( o == null ) return false;
  	if ( this.getClass () != o.getClass () ) return false;
  		
    // Compare accessions if both are non-null, use identity otherwise
    CVTerm that = (CVTerm) o;
    return ( this.getName () == null | that.getName () == null ) ? false : this.name.equals ( that.name );
  }
  
  @Override
  public int hashCode()  {
  	return this.getName () == null ? super.hashCode () : this.name.hashCode ();
  }

  @Override
	public String toString() {
		return this.getClass ().getSimpleName () + "{ id: " + this.getId() + ", name: '" + this.getName () + "' }";
	}

}
