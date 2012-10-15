package uk.ac.ebi.fg.core_model.organizational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.DefaultAnnotatable;


/**
 * 
 * The representation of an organisation, such as a department or a university.
 *
 * Setters like {@link #setUrl(String)}, {@link #setPhone(String)} are treated as free-text strings and not checked for
 * consistency. This is up to the application.
 * 
 * Organisation equivalence is the object identity, since usually organisations are not re-used (unless complex 
 * reconciliation is in place, which would not use equivalence methods any way).
 *
 * <dl><dt>date</dt><dd>Jun 5, 2012</dd></dl>
 * @author brandizi
 *
 */
@Entity
@Table(name = "organization")
public class Organization extends DefaultAnnotatable
{
	private String name;
	private String description;
	private String address;
	private String url;
	private String email;
	private String phone;
	private String fax;


	private Set<ContactRole> organizationRoles = new HashSet<ContactRole> ();
	
  @Index( name = "org_name" )
  @Column ( length = Const.COL_LENGTH_L )
  public String getName ()
	{
		return name;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

  @Index( name = "org_descr" )
  @Column ( length = Const.COL_LENGTH_XL )
	public String getDescription ()
	{
		return description;
	}

	public void setDescription ( String description )
	{
		this.description = description;
	}

  @Column ( length = Const.COL_LENGTH_XL )
	public String getAddress ()
	{
		return address;
	}

	public void setAddress ( String address )
	{
		this.address = address;
	}

  @Index( name = "org_url" )
  @Column ( length = 255 )
	public String getUrl ()
	{
		return url;
	}

	public void setUrl ( String url )
	{
		this.url = url;
	}

  @Index( name = "org_email" )
  @Column ( length = Const.COL_LENGTH_S )
	public String getEmail ()
	{
		return email;
	}

	public void setEmail ( String email )
	{
		this.email = email;
	}

  @Column ( length = Const.COL_LENGTH_S )
	public String getPhone ()
	{
		return phone;
	}

	public void setPhone ( String phone )
	{
		this.phone = phone;
	}

  @Column ( length = Const.COL_LENGTH_S )
	public String getFax ()
	{
		return fax;
	}

	public void setFax ( String fax )
	{
		this.fax = fax;
	}

  @ManyToMany( targetEntity = ContactRole.class, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
    name = "organization_roles", 
    joinColumns = {@JoinColumn( name = "organization_id" )}, inverseJoinColumns = @JoinColumn( name = "role_id" ))
	public Set<ContactRole> getOrganizationRoles() {
    return organizationRoles;
  }

  /** 
   * This allows you to get a contact-role of a different sub-class. We need to implement this separately than
   * {@link #getOrganizationRoles()}, because Hibernate doesn't like generic wild cards in method signatures.  
   */
  @Transient
  public Set<? extends ContactRole> getOrganizationRolesAsGeneric () {
    return getOrganizationRoles ();
  }

  
  /**
   * @see #addOrganizationRole(ContactRole)
   */
	protected void setOrganizationRoles ( Set<ContactRole> organizationRoles ) {
    this.organizationRoles = organizationRoles;
  }

  /**
   *  The role-related methods allows for the addition of a {@link ContactRole} extension, cause one may want the 
   *  latter to be an OntologyEntry and not simply a CVTerm.
   *  
   */
  public boolean addOrganizationRole ( ContactRole organizationRole ) {
    return organizationRoles.add ( organizationRole );
  }

  /**
   * @see #addOrganizationRole(ContactRole)
   */
  public boolean removeOrganizationRole ( ContactRole organizationRole ) {
    return organizationRoles.remove ( organizationRole );
  }

  /**
   * @see #addOrganizationRole(ContactRole)
   */
  public boolean containsOrganizationRole ( ContactRole organizationRole ) {
    return organizationRoles.contains ( organizationRole );
  }
  

  public String toString() 
  {
  	return String.format ( 
  		"%s { id: %d, name: '%s', description: '%s', url: '%s', email: '%s', address: '%s', phone: '%s', fax: '%s', roles: %s }",
  		this.getClass ().getSimpleName (), this.getId (),
  		this.getName (), this.getDescription (), this.getUrl (), this.getEmail (), this.getAddress (), 
  		this.getPhone (), this.getFax (), getOrganizationRoles ()
  	);
  }
  
}
