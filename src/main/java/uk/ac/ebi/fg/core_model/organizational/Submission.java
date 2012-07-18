package uk.ac.ebi.fg.core_model.organizational;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import uk.ac.ebi.fg.core_model.toplevel.DefaultAccessibleAnnotatable;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * The concept of data submission. What this exactly is depends on how the creation of new data is managed in your system. 
 * For instance, a set of files corresponding to one experiment is considered a submission in the context of the MAGE-TAB
 * format. A different set of files where multiple sample groups and samples are reported is a sunmission in the 
 * Sample-TAB format. You'll probably want to extend this class and make a custom version to suit your specific needs. 
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@Entity
@Table ( name = "submission" )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "submission_seq" )
@AssociationOverride ( name = "annotations", joinTable = @JoinTable( name = "submission_annotation" ) )
public class Submission extends DefaultAccessibleAnnotatable
{
	private String title;
	private String description;
	private String version;
	private String formatVersion;
	private Date releaseDate;
	private Date updateDate;
	private Date submissionDate;
	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	@JoinColumn ( name = "submission_id" )
	private Set<Contact> contacts = new HashSet<Contact> ();
	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	@JoinColumn ( name = "submission_id" )
	private Set<Organization> organizations = new HashSet<Organization> ();
	
	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	@JoinColumn ( name = "submission_id" )
	private Set<Publication> publications = new HashSet<Publication> ();

	@OneToMany ( cascade = {CascadeType.ALL}, orphanRemoval = true )
	@JoinColumn ( name = "submission_id" )
	private Set<ReferenceSource> referenceSources = new HashSet<ReferenceSource> ();
	
	
	
	protected Submission () {
		super ();
	}

	public Submission ( String acc ) {
		super ( acc );
	}


	public String getTitle ()
	{
		return title;
	}
	
	public void setTitle ( String title )
	{
		this.title = title;
	}
	
	public String getDescription ()
	{
		return description;
	}
	
	public void setDescription ( String description )
	{
		this.description = description;
	}
	
	public String getVersion ()
	{
		return version;
	}
	
	public void setVersion ( String version )
	{
		this.version = version;
	}
	
	public String getFormatVersion ()
	{
		return formatVersion;
	}

	public void setFormatVersion ( String formatVersion )
	{
		this.formatVersion = formatVersion;
	}

	public Date getReleaseDate ()
	{
		return releaseDate;
	}
	
	public void setReleaseDate ( Date releaseDate )
	{
		this.releaseDate = releaseDate;
	}
	
	public Date getUpdateDate ()
	{
		return updateDate;
	}
	
	public void setUpdateDate ( Date updateDate )
	{
		this.updateDate = updateDate;
	}
	
	public Date getSubmissionDate ()
	{
		return submissionDate;
	}
	
	public void setSubmissionDate ( Date submissionDate )
	{
		this.submissionDate = submissionDate;
	}
	
	public Set<Contact> getContacts ()
	{
		return contacts;
	}
	
	public void setContacts ( Set<Contact> contacts )
	{
		this.contacts = contacts;
	}
	
	public boolean addContact ( Contact contact ) {
		return this.contacts.add ( contact );
	}
	
	
	public Set<Organization> getOrganizations ()
	{
		return organizations;
	}
	
	public void setOrganizations ( Set<Organization> organizations )
	{
		this.organizations = organizations;
	}
	
	public boolean addOrganization ( Organization org ) {
		return this.organizations.add ( org );
	}

	
	public Set<Publication> getPublications ()
	{
		return publications;
	}
	
	public void setPublications ( Set<Publication> publications )
	{
		this.publications = publications;
	}
	
	public boolean addPublication ( Publication pub ) {
		return this.publications.add ( pub );
	}

	
	
	public Set<ReferenceSource> getReferenceSources ()
	{
		return referenceSources;
	}
	
	public void setReferenceSources ( Set<ReferenceSource> referenceSources )
	{
		this.referenceSources = referenceSources;
	}

	public boolean addReferenceSource ( ReferenceSource ref ) {
		return this.referenceSources.add ( ref );
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
