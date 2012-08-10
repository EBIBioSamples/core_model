package uk.ac.ebi.fg.core_model.toplevel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * An identifiable object is anything with a database-assigned ID.  Every
 * database object should extend this class.
 * 
 * <dl><dt>Date</dt><dd>Jul 10, 2007</dd></dl>
 *
 * @author Nataliya Sklyar
 * @author Tony Burdett
 * @author Marco Brandizi (migrated to AE2 and revised in 2012)
 * 
 * 
 */
@MappedSuperclass
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class Identifiable 
{
  private Long id;

  /**
   * Get the id of this identifiable object.  This is the database id.
   */
  @Id
  @GeneratedValue ( strategy = GenerationType.TABLE )
  public Long getId () {
    return id;
  }

  /**
   * Set the id of this identifiable object.  This id is the oracle database id.
   * You should never explicitly set this, Hibernate will handle the creation of this ID whenever a new object is saved.
   * 
   */
  protected void setId ( Long id ) 
  {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.getClass ().getSimpleName () + "{ id: " + getId() + "}";
  }
  
}
