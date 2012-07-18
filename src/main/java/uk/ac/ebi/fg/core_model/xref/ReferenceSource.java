package uk.ac.ebi.fg.core_model.xref;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import uk.ac.ebi.fg.core_model.toplevel.Accessible;
import uk.ac.ebi.fg.core_model.toplevel.Identifiable;


/**
 * 
 * The source of any external reference. For instance, a record that represent the ENA database (the whole DB). Or, the
 * source of an ontology, such as EFO or GO. In the context of this model, it is assumed that every source has a different
 * accession, presumably assigned by the database curators.
 * 
 * Note that this class doesn't extend {@link Accessible}, because in general you need both an accession and a version 
 * to identify a source. It's up to the application to be such fine-grained or to assume unique accessions for reference
 * sources.  
 * 
 *
 * <dl><dt>date</dt><dd>Jun 14, 2012, imported from the AE2 model</dd></dl>
 * 
 * @author Nataliya Sklyar
 * @author Marco brandizi
 *
 */
@Entity
@Table( name = "reference_source", uniqueConstraints = @UniqueConstraint ( columnNames = { "acc", "version" } ) )
@SequenceGenerator ( name = "hibernate_seq", sequenceName = "reference_source_seq" )
@org.hibernate.annotations.Table ( 
	appliesTo = "reference_source", 
	indexes = {	@Index ( name = "reference_source_av", columnNames = { "acc", "version" } ) }
)
public class ReferenceSource extends Identifiable
{
	@Column( unique = false, nullable = false )
  private String acc;

  private String name;
  private String url;
  private String version;
  private String description;

  protected ReferenceSource() {
  }

  public ReferenceSource ( String acc, String version ) {
  	this.acc = acc;
  	this.version = version;
  }

	public String getAcc ()
	{
		return acc;
	}

	protected void setAcc ( String acc )
	{
		this.acc = acc;
	}

	public String getName ()
	{
		return name;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl ( String url )
	{
		this.url = url;
	}

	public String getVersion ()
	{
		return version;
	}

	protected void setVersion ( String version )
	{
		this.version = version;
	}

	public String getDescription ()
	{
		return description;
	}

	public void setDescription ( String description )
	{
		this.description = description;
	}

  /**
   * If either accession is null (which should never happen), they're different. If they've the same non null accession,  
   * it compares the versions too.
   * 
   */
  @Override
  public boolean equals ( Object o ) 
  {
  	if ( this == o ) return true;
  	if ( this.getClass () != o.getClass () ) return false;
  	
    // Compare accessions if both are non-null, use identity otherwise
  	ReferenceSource that = (ReferenceSource) o;
    if ( this.getAcc () == null || that.getAcc () == null || !this.version.equals ( that.acc ) ) 
    	return false;
    return this.getVersion () != null ? !this.version.equals ( that.getVersion () ) : that.getVersion () != null; 
  }
  
  @Override
  public int hashCode() 
  {
  	return this.getAcc () == null 
  		? super.hashCode () 
  		: this.acc.hashCode () * 31 + ( this.getVersion () == null ? 0 : this.version.hashCode () );
  }	
	
	
	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %d, acc: '%s', name: '%s', url: '%s', version: '%s', description: '%.15s' }",
			this.getClass ().getSimpleName (), getId (), getAcc (), getName (), getUrl (), getVersion (), getDescription () 
		);
	}

}
