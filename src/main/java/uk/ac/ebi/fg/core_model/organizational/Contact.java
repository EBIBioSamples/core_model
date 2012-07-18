package uk.ac.ebi.fg.core_model.organizational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "contact_seq" )
@AssociationOverride ( name = "annotations", joinTable = @JoinTable( name = "contact_annotation" ) )
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

  @ManyToMany( targetEntity = ContactRole.class, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
    name = "contact_roles", 
    joinColumns = {@JoinColumn( name = "contact_id" )}, inverseJoinColumns = @JoinColumn( name = "role_id" ))
  private Set<ContactRole> contactRoles = new HashSet<ContactRole>();

  
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @see #setMidInitials(String)
   */
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

  public String getUrl() {
    return url;
  }

  /**
   * A web site associated to the person, such as his/her personal site or summary page.
   *    
   */
  public void setUrl(String url) {
    this.url = url;
  }

  public Set<? extends ContactRole> getContactRoles() {
    return contactRoles;
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
