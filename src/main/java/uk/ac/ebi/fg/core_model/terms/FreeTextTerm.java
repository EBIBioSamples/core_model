package uk.ac.ebi.fg.core_model.terms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import uk.ac.ebi.fg.core_model.toplevel.Identifiable;

/**
 * A free-text annotation that can optionally be linked to existing ontology terms. An example of where this is useful
 * is curated submissions, where initially provided properties are enriched by curators (or their computational tools) 
 * with ontology annotations. 
 * 
 * @author Nataliya Sklyar
 * @author Tony Burdett
 * @author mdylag
 * @author niran
 * @author Marco Brandizi
 * @date Jul 12, 2007, imported from the AE2 model in 2012
 *  
 */
@MappedSuperclass
public abstract class FreeTextTerm extends Identifiable 
{
  private String termText;
  private Set<OntologyEntry> ontologyTerms = new HashSet<OntologyEntry> ();
  
  public FreeTextTerm () {
		super ();
	}

  public FreeTextTerm ( String termText )
	{
		super ();
		this.termText = termText;
	}

  @Transient // This is to be mapped in the descendants, cause we want different field lengths
	public String getTermText ()
	{
		return termText;
	}

	public void setTermText ( String termText )
	{
		this.termText = termText;
	}

	/**
	 * TODO: At the moment join table names are auto-generated with the '_ontology_entry' postfix. This sounds too long and
	 * probably it would be a good idea to override the association at subclass level and redefine the table names using the
	 * _oe postfix.
	 *
	 */
	@ManyToMany( targetEntity = OntologyEntry.class, 
							 cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
	public <OE extends OntologyEntry> Set<OE> getOntologyTerms () {
  	return (Set<OE>) this.ontologyTerms;
  }

  public <OE extends OntologyEntry> void setOntologyTerms ( Set<OE> ontologyTerms ) {
  	this.ontologyTerms = (Set<OntologyEntry>) ontologyTerms;
  }

  public boolean addOntologyTerm ( OntologyEntry ontologyTerm ) {
  	return this.ontologyTerms.add ( ontologyTerm );
  }

  public boolean removeOntologyTerm ( OntologyEntry ontologyTerm ) {
  	return this.ontologyTerms.remove ( ontologyTerm );
  }

  public boolean containsOntologyTerm ( OntologyEntry term ) {
  	return this.containsOntologyTerm ( term );
  }

	/**
	 * @return the first term returned by {@link #getOntologyTerms()} (the result is undetermined if there is more than 
	 * one).
	 * 
	 */
	@Transient
	public <OE extends OntologyEntry> OE getSingleOntologyTerm () 
	{
		Iterator<OntologyEntry> itr = getOntologyTerms ().iterator ();
		return itr.hasNext () ? (OE) itr.next () : null;
	}

	@Override
	public String toString ()
	{
		return String.format ( 
			"%s { id: %d, termText: '%s', ontology terms:\n  %s\n}", 
			this.getClass ().getSimpleName (), this.getId (), this.getTermText (), this.getOntologyTerms () 
		);
	}

}
