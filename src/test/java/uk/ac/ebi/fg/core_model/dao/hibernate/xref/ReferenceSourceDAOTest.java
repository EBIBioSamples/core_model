/*
 * 
 */
package uk.ac.ebi.fg.core_model.dao.hibernate.xref;

import org.junit.Before;
import org.junit.Rule;

import uk.ac.ebi.fg.core_model.resources.Resources;
import uk.ac.ebi.utils.test.junit.TestEntityMgrProvider;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Aug 10, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ReferenceSourceDAOTest
{
	@Rule
	public TestEntityMgrProvider emProvider = new TestEntityMgrProvider ( Resources.getInstance ().getEntityManagerFactory () );

	@Before
	public void init()
	{
		
	}
}
