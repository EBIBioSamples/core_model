package uk.ac.ebi.fg.core_model.organizational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.DefaultAnnotatable;

/**
 * A contact, to be used to model a person who is linked to a representation of biomedical data, such as an experiment's
 * author or a bio-sample provider.
 * 
 * Contact equality is based on identity, because usually they are not re-used (unless complex 
 * reconciliation is in place, which would not use equivalence methods any way).
 * 
 * @author Marco Brandizi
 * @author Tony Burdett
 * @date 28th April 2009, imported from AE2 in June 2012
 *
 */
@Entity
@Table(name = "contact")
@Inheritance ( strategy = InheritanceType.TABLE_PER_CLASS )
public class Contact extends DefaultAnnotatable
{
  private String firstName;
  private String lastName;
  private String midInitials;
  private String email;
  private String phone;
  private String fax;
  private String address;
  private String affiliation;
  private String url;

  private Set<ContactRole> contactRoles = new HashSet<ContactRole>();
  
  @Index( name = "cnt_name" )
  @Column ( length = Const.COL_LENGTH_M )
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Index( name = "cnt_surname" )
  @Column ( length = Const.COL_LENGTH_M )
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @see #setMidInitials(String)
   */
  @Index( name = "cnt_mid" )
  @Column ( length = Const.COL_LENGTH_S )
  public String getMidInitials() {
    return midInitials;
  }

  /**
   * Get any middle initials of the contact.  This is formatted as a single
   * string and there is no prescribed format for how data is entered here.  So,
   * for example, multiple initials may be separated by spaces, dots, or some
   * other convention.
   * 
   */
  public void setMidInitials(String midInitials) {
    this.midInitials = midInitials;
  }

  /**
   * @see #setEmail(String)
   */
  @Column ( length = Const.COL_LENGTH_S )
  @Index( name = "cnt_email" )
  public String getEmail() {
    return email;
  }

  /**
   * Set the email address of this contact.  This is formatted as a single
   * string, so no validation checks that this is a valid email address are
   * done.  Client applications should implement code to do this.
   *
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @see #setPhone(String)
   */
  @Column ( length = Const.COL_LENGTH_S )
  public String getPhone() {
    return phone;
  }

  /**
   * Set the phone number of this contact. It's a free-text string, with no checking here 
   * (should be done by applications).
   * 
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * @see #setFax(String)
   */
  @Column ( length = Const.COL_LENGTH_S )
  public String getFax() {
    return fax;
  }

  /**
   * Set the fax number of this contact. It's a free-text string, with no checking here
   * (should be done by applications).
   * 
   */
  public void setFax(String fax) {
    this.fax = fax;
  }

  @Column ( length = Const.COL_LENGTH_XL )
  public String getAddress() {
    return address;
  }

  /**
   * This may add up to Organization
   * @param address
   */
  public void setAddress ( String address ) {
    this.address = address;
  }

  /**
   * @see #setAffiliation(String) 
   */
  @Index( name = "cnt_affiliation" )
  @Column ( length = Const.COL_LENGTH_L )
  public String getAffiliation() {
    return affiliation;
  }

  /**
   * This may be a free-text string that is used as an alternative to Organization, or additional information to 
   * that (e.g., department).
   * 
   */
  public void setAffiliation ( String affiliation ) {
    this.affiliation = affiliation;
  }

  @Index( name = "cnt_url" )
  @Column ( length = 255 )
  public String getUrl() {
    return url;
  }

  /**
   * A web site associated to the person, such as his/her personal site or summary page.
   *    
   */
  public void setUrl ( String url ) {
    this.url = url;
  }

  /**
   * @see #getContactRolesAsGeneric().
   * @return
   */
  @ManyToMany( targetEntity = ContactRole.class, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
    name = "contact_roles", 
    joinColumns = {@JoinColumn( name = "contact_id" )}, inverseJoinColumns = @JoinColumn( name = "role_id" ))
  public Set<ContactRole> getContactRoles() {
    return contactRoles;
  }

  /** 
   * This allows you to get a contact-role of a different sub-class. We need to implement this separately than
   * {@link #getContactRoles()}, because Hibernate doesn't like generic wild cards in method signatures.  
   */
  @Transient
  public Set<? extends ContactRole> getContactRolesAsGeneric () {
    return getContactRoles ();
  }

  /**
   * @see #addContactRole(ContactRole)
   */
	public void setContactRoles ( Set<ContactRole> contactRoles ) {
    this.contactRoles = contactRoles;
  }

  /**
   *  The role-related methods allows for the addition of a {@link ContactRole} extension, cause one may want the 
   *  latter to be an OntologyEntry and not simply a CVTerm.
   *  
   */
  public boolean addContactRole ( ContactRole contactRole ) {
    return contactRoles.add ( contactRole );
  }

  /**
   * @see #addContactRole(ContactRole)
   */
  public boolean removeContactRole ( ContactRole contactRole ) {
    return contactRoles.remove ( contactRole );
  }

  /**
   * @see #addContactRole(ContactRole)
   */
  public boolean containsContactRole ( ContactRole contactRole ) {
    return contactRoles.contains ( contactRole );
  }


	public String toString() 
  {
  	return String.format ( 
  		"%s { id: %d, name: '%s', '%s', '%s', email: '%s', phone: '%s', fax: '%s', address: '%s', affiliation: '%s', roles: %s }",
  		this.getClass ().getSimpleName (), this.getId (),
  		this.getFirstName (), this.getMidInitials (), this.getLastName (), 
  		this.getEmail (), this.getPhone (), this.getFax (), this.getAddress (), this.getAffiliation (), 
  		this.getContactRoles ()
  	);
  }
}
