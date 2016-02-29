package uk.ac.ebi.fg.core_model.organizational;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.resources.Const;
import uk.ac.ebi.fg.core_model.toplevel.Accessible;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * The concept of data submission. What this exactly is depends on how the creation of new data is managed in your system. 
 * For instance, a set of files corresponding to one experiment is considered a submission in the context of the MAGE-TAB
 * format. A different set of files where multiple sample groups and samples are reported is a submission in the 
 * Sample-TAB format. Because what a submission is concretely varies greatly, this class is made abstract. 
 * Extend it to suit your specific needs. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@MappedSuperclass
public abstract class Submission extends Accessible
{
	private String title;
	private String description;
	private String version;
	private String formatVersion;
	private Date releaseDate;
	private Date updateDate;
	private Date submissionDate;
	
	private Set<Contact> contacts = new HashSet<Contact> ();
	private Set<Organization> organizations = new HashSet<Organization> ();
	private Set<Publication> publications = new HashSet<Publication> ();
	private Set<ReferenceSource> referenceSources = new HashSet<ReferenceSource> ();
	
	protected Submission () {
		super ();
	}

	public Submission ( String acc ) {
		super ( acc );
	}

	@Column ( length = Const.COL_LENGTH_L )
	@Index ( name = "abstr_sub_title" )
	public String getTitle ()
	{
		return title;
	}
	
	public void setTitle ( String title )
	{
		this.title = title;
	}
	
	@Lob
	public String getDescription ()
	{
		return description;
	}
	
	public void setDescription ( String description )
	{
		this.description = description;
	}
	
  @Column ( length = Const.COL_LENGTH_S )
	public String getVersion ()
	{
		return version;
	}
	
	public void setVersion ( String version )
	{
		this.version = version;
	}
	
   @Column ( name= "formatversion", length = Const.COL_LENGTH_S )
	public String getFormatVersion ()
	{
		return formatVersion;
	}

	public void setFormatVersion ( String formatVersion )
	{
		this.formatVersion = formatVersion;
	}

	@Column ( name = "release_date", nullable = true )
	@Index ( name = "abstr_sub_rel_date" )
	public Date getReleaseDate ()
	{
		return releaseDate;
	}
	
	public void setReleaseDate ( Date releaseDate )
	{
		this.releaseDate = releaseDate;
	}
	
	@Column ( name = "update_date" )
	@Index ( name = "abstr_sub_up_date" )
	public Date getUpdateDate ()
	{
		return updateDate;
	}
	
	public void setUpdateDate ( Date updateDate )
	{
		this.updateDate = updateDate;
	}
	
	@Column ( name = "submission_date" )
	@Index ( name = "abstr_sub_sub_date" )
	public Date getSubmissionDate ()
	{
		return submissionDate;
	}
	
	public void setSubmissionDate ( Date submissionDate )
	{
		this.submissionDate = submissionDate;
	}
	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	/*
	 * Leaving Hibernate to define this is much simpler, it avoids to re-dedefine it all
	 * in subclasses.
	 *  
	 * JoinTable ( name = "submission_contact", 
	 * joinColumns = @JoinColumn ( name = "submission_id" ), inverseJoinColumns = @JoinColumn ( name = "contact_id" ) )
	 */
	public Set<Contact> getContacts ()
	{
		return contacts;
	}
	
	public void setContacts ( Set<Contact> contacts )
	{
		this.contacts = contacts;
	}
	
	public boolean addContact ( Contact contact ) {
		return this.getContacts ().add ( contact );
	}
	
	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	public Set<Organization> getOrganizations () {
		return organizations;
	}
	
	public void setOrganizations ( Set<Organization> organizations ) {
		this.organizations = organizations;
	}
	
	public boolean addOrganization ( Organization org ) {
		return this.getOrganizations ().add ( org );
	}

	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	public Set<Publication> getPublications () {
		return publications;
	}
	
	public void setPublications ( Set<Publication> publications ) {
		this.publications = publications;
	}
	
	public boolean addPublication ( Publication pub ) {
		return this.getPublications ().add ( pub );
	}

	
	
	@ManyToMany ( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	public Set<ReferenceSource> getReferenceSources () {
		return referenceSources;
	}
	
	public void setReferenceSources ( Set<ReferenceSource> referenceSources ) {
		this.referenceSources = referenceSources;
	}

	public boolean addReferenceSource ( ReferenceSource ref ) {
		return this.getReferenceSources ().add ( ref );
	}

	
	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %s, acc: '%s', title: '%s', description: '%s', version: '%s', sub. date: '%s', rel. date: '%s', " +
			"update date: '%s', format ver.: '%s', contacts:\n  %s,\n organizations:\n  %s,\n ref sources:\n  %s}",
			this.getClass ().getSimpleName (), this.getId (), this.getAcc (), this.getTitle (), 
			StringUtils.abbreviate ( this.getDescription (), 20 ), this.getVersion (), this.getSubmissionDate (),
			this.getReleaseDate (), this.getUpdateDate (), this.getFormatVersion (), this.getContacts (), 
			this.getOrganizations (), this.getReferenceSources ()
		);
	}
}
