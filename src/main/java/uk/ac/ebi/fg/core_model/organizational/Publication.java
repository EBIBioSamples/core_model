package uk.ac.ebi.fg.core_model.organizational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.ac.ebi.fg.core_model.toplevel.DefaultAnnotatable;
import uk.ac.ebi.fg.core_model.xref.Referrer;
import uk.ac.ebi.fg.core_model.xref.XRef;


/**
 * 
 * A representation of a publication, such as a scientific article.
 * 
 * Publication equality is based on identity, because usually they are not re-used (unless complex 
 * reconciliation is in place, which would not use equivalence methods any way).

 * <dl><dt>date</dt><dd>28th April 2009, imported from AE on June 2012</dd></dl>
 *
 * @author Marco Brandizi
 * @author Tony Burdett
 * @author Niran Abeygunawardena
 */

@Entity
@Table(name = "publication")
@SequenceGenerator( name = "hibernate_seq", sequenceName = "publication_seq" )
@AssociationOverride ( name = "annotations", joinTable = @JoinTable( name = "publication_annotation" ) )
public class Publication extends DefaultAnnotatable implements Referrer
{
  private String title;
  private String authorList;
  private String doi;
  private String pubMedId;
  private String journal;
  private String publisher;
  private String editor;
  private String year;
  private String volume;
  private String issue;
  private String pages;
  private String uri;

  @Embedded
	@ElementCollection
	@CollectionTable( name = "referrer_annotation", joinColumns = @JoinColumn( name = "owner_id" ) )
	private Set<XRef> references = new HashSet<XRef> ();
  
  @ManyToOne(
  	targetEntity = PublicationStatus.class,
    cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn( name = "status_id", nullable = true )
  private PublicationStatus status;
  
  
  /**
   * It means a journal without any public accession (DOI or PMID). Any two publications of this type are considered
   * different. 
   */
  public Publication ()
	{
		super ();
	}

  /**
   * A journal with either or both public accessions (DOI and/or PUBMED ID). Two publications of this type
   * are considered equals if both accessions are equals (if they're not null) or one type of accessions are
   * equal and the other types are null. Eg. doi1 = doi2 and pmid1 = pmid2 = (value or null).
   * 
   */
	public Publication ( String doi, String pubMedId )
	{
		super ();
		this.doi = doi;
		this.pubMedId = pubMedId;
	}


	public String getTitle ()
	{
		return title;
	}


	public void setTitle ( String title )
	{
		this.title = title;
	}


	public String getAuthorList ()
	{
		return authorList;
	}


	public void setAuthorList ( String authorList )
	{
		this.authorList = authorList;
	}


	public String getDOI ()
	{
		return doi;
	}


	protected void setDOI ( String doi )
	{
		this.doi = doi;
	}


	public String getPubMedId ()
	{
		return pubMedId;
	}


	protected void setPubmedId ( String pubMedId )
	{
		this.pubMedId = pubMedId;
	}


	/**
	 * @see #setJournal(String)
	 */
	public String getJournal ()
	{
		return journal;
	}

	/**
	 * The journal or similar entity (eg, proceedings, book)
	 */
	public void setJournal ( String journal )
	{
		this.journal = journal;
	}


	public String getPublisher ()
	{
		return publisher;
	}


	public void setPublisher ( String publisher )
	{
		this.publisher = publisher;
	}


	public String getEditor ()
	{
		return editor;
	}


	public void setEditor ( String editor )
	{
		this.editor = editor;
	}


	public String getYear ()
	{
		return year;
	}


	public void setYear ( String year )
	{
		this.year = year;
	}


	public String getVolume ()
	{
		return volume;
	}


	public void setVolume ( String volume )
	{
		this.volume = volume;
	}


	public String getIssue ()
	{
		return issue;
	}


	public void setIssue ( String issue )
	{
		this.issue = issue;
	}


	public String getPages ()
	{
		return pages;
	}


	public void setPages ( String pages )
	{
		this.pages = pages;
	}


	public String getUri ()
	{
		return uri;
	}


	public void setUri ( String uri )
	{
		this.uri = uri;
	}


	public PublicationStatus getStatus ()
	{
		return status;
	}


	public void setStatus ( PublicationStatus status )
	{
		this.status = status;
	}
	
	@Override
	public Set<XRef> getReferences ()
	{
		return this.references;
	}

	@Override
	public void setReferences ( Set<XRef> xrefs )
	{
		this.references = xrefs;
	}

	@Override
	public void addReference ( XRef xref )
	{
		this.references.add ( xref );
	}
	
	
	/**
	 * They're equivalent iff they are the same object or both DOIs and PMIDs are equivalent one each other. This include
	 * the case one of the two identifier types are equivalent and the other are both null. If both the DOIs and the PMIDs
	 * are defined by either pair is different, the publications are not considered equal (this condition is actually 
	 * an inconsistency, it should be validated elsewhere and avoided. 
	 *   
	 */
	@Override
	public boolean equals ( Object o )
	{
  	if ( this == o ) return true;
  	if ( ! ( o instanceof Publication ) ) return false;
  	
    // Compare accessions if both are non-null, use identity otherwise
  	Publication that = (Publication) o;
  	
  	// TODO: if the DOIs are equals then PUBMED IDs should be equal too
  	that.getDOI ();
  	
  	if ( this.getDOI () != null && this.doi.equals ( that.doi ) ) 
  		return this.getPubMedId () != null ? this.pubMedId.equals ( that.getPubMedId () ) : that.getPubMedId () == null;
  	
  	// DOIs are different, both can be null
  	if ( this.getPubMedId () != null && this.pubMedId.equals ( that.getPubMedId () ) )
  		return this.doi == null && that.doi == null;
  	
  	// Otherwise use the identity
  	return false;
	}


	@Override
	public int hashCode ()
	{
		if ( getDOI () != null ) {
			int result = getDOI ().hashCode () * 31;
			return getPubMedId () == null ? result : result + pubMedId.hashCode ();
		}
		
		// DOI is null
		return getPubMedId () == null ? super.hashCode () : pubMedId.hashCode ();
	}

	@Override
	public String toString ()
	{
		return String.format (
			"%s { id: %id, title: '%s', authors: '%s', year: '%s', PMID: '%s', DOI: '%s', status: '%s', URI: '%s' }", 
			this.getClass ().getName (), this.getId (), this.getTitle (), this.getAuthorList (), this.getYear (), 
			this.getPubMedId (), this.getDOI (), this.getStatus (), this.getUri ()
		);
	}
	
	
}
