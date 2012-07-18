package uk.ac.ebi.fg.core_model.toplevel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;

import uk.ac.ebi.fg.core_model.organizational.Contact;

/**
 * 
 * A default implementation of {@link Accessible} combined with {@link Annotatable}. As in similar classes, you'll need
 * to review some ORM annotations when extending this class, see {@link #annotations}.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public abstract class DefaultAccessibleAnnotatable extends Accessible implements Annotatable
{
	
	protected DefaultAccessibleAnnotatable () {
		super ();
	}

	public DefaultAccessibleAnnotatable ( String acc ) {
		super ( acc );
	}

	/**
	 * You need to change the table name with {@link AssociationOverride#joinTable()}. Have a look at {@link Contact}
	 * for an example.
	 */
  @Embedded
	@ElementCollection
	@CollectionTable( name = "annotatable_annotation", joinColumns = @JoinColumn( name = "owner_id" ) )
	private Set<Annotation> annotations = new HashSet<Annotation>();
  
  @Override
  public void addAnnotation ( Annotation annotation ) {
  	annotations.add ( annotation );
  }

  @Override
  public Set<Annotation> getAnnotations() {
  	return annotations;
  }

  @Override
  public void setAnnotations( Set<Annotation> annotations ) {
  	this.annotations = annotations;
  }
}
