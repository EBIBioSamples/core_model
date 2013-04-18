package uk.ac.ebi.fg.core_model.toplevel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * 
 * A default implementation of {@link Accessible} combined with {@link Annotatable}. As in similar classes, you'll need
 * to review some ORM annotations when extending this class, see {@link #annotations}. @see {@link Annotatable} for 
 * notes on the references relationship.
 *
 * <dl><dt>date</dt><dd>Jul 17, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@MappedSuperclass
public abstract class DefaultAccessibleAnnotatable extends Accessible implements Annotatable
{
	
	protected DefaultAccessibleAnnotatable () {
		super ();
	}

	public DefaultAccessibleAnnotatable ( String acc ) {
		super ( acc );
	}

	private Set<Annotation> annotations = new HashSet<Annotation>();
  
  @Override
  public void addAnnotation ( Annotation annotation ) {
  	annotations.add ( annotation );
  }

  @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
  @JoinTable ( joinColumns = @JoinColumn ( name = "owner_id" ), inverseJoinColumns = @JoinColumn ( name = "annotation_id" ) )
  @Override
	public Set<Annotation> getAnnotations () {
		return this.annotations;
	}

  @Override
  public void setAnnotations( Set<Annotation> annotations ) {
  	this.annotations = annotations;
  }
}
