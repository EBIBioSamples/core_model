package uk.ac.ebi.fg.core_model.resources;

/**
 * Some commonly used constants.
 *
 * <dl><dt>date</dt><dd>Sep 24, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
public class Const
{
	/** Length used for database character columns, e.g., accession, contact's mid title, version */
	public static final int COL_LENGTH_S = 50;

	/** Length used for database character columns, e.g., name, surname */
	public static final int COL_LENGTH_M = 100;

	/** Length used for database character columns, e.g., title */
	public static final int COL_LENGTH_L = 300;

	/** Length used for database character columns, e.g., address, citation, (short) description */
	public static final int COL_LENGTH_XL = 1000;
	
	/** Length used for URIs and the like */
	public static final int COL_LENGTH_URIS = 2000;

}
