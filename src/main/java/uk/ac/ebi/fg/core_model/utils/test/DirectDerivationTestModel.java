/*
 * 
 */
package uk.ac.ebi.fg.core_model.utils.test;

import java.lang.reflect.Field;
import java.util.HashSet;

import javax.persistence.EntityManager;

import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;
import uk.ac.ebi.fg.core_model.expgraph.BioMaterialProcessing;
import uk.ac.ebi.fg.core_model.expgraph.Data;
import uk.ac.ebi.fg.core_model.expgraph.Product;
import uk.ac.ebi.fg.core_model.expgraph.properties.BioCharacteristicType;
import uk.ac.ebi.fg.core_model.expgraph.properties.BioCharacteristicValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.DataPropertyType;
import uk.ac.ebi.fg.core_model.expgraph.properties.DataPropertyValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.Unit;
import uk.ac.ebi.fg.core_model.expgraph.properties.UnitDimension;
import uk.ac.ebi.fg.core_model.persistence.dao.hibernate.toplevel.AccessibleDAO;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * A mock-up model to be used for testing purposes.
 *
 * <dl><dt>date</dt><dd>Sep 3, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class DirectDerivationTestModel
{
	public BioMaterial bm1;
	public BioMaterial bm2;
	public BioMaterialProcessing proc1;
	public BioMaterial bm3;
	public Data data1;
	public Data data2;
	public Data data3;
	public Data data4;
	public BioCharacteristicType ch1;
	public BioCharacteristicValue cv1;
	public BioCharacteristicType ch2;
	public BioCharacteristicValue cv2;
	public BioCharacteristicValue cv3;
	public BioCharacteristicValue cv4;
	public UnitDimension timeDim;
	public Unit monthsUnit;
	public DataPropertyType dp1;
	public DataPropertyValue dpv1;
	
	
	/**
	 * Calls {@link #DirectDerivationTestModel(String)} with "".
	 */
	public DirectDerivationTestModel () {
		this ( "" );
	}

	/**
	 * 	<pre>
	 *  bm1 -----> bm3 ---> data1 ---> data3
	 *  bm2 ---/      \---> data2 --/        \--> data4
	 *  </pre>        
	 */
	public DirectDerivationTestModel ( String prefix )
	{
		bm1 = new BioMaterial ( prefix + "bm1" );
		bm2 = new BioMaterial ( prefix + "bm2" );
		bm3 = new BioMaterial ( prefix + "bm3" );

		data1 = new Data ( prefix + "data1" );
		data2 = new Data ( prefix + "data2" );
		data3 = new Data ( prefix + "data3" );
		data4 = new Data ( prefix + "data4" );
		
		// These relations are symmetric
		bm3.addDerivedFrom ( bm1 );
		bm2.addDerivedInto ( bm3 );

		data1.addDerivedFrom ( bm3 );
		data2.addDerivedFrom ( bm3 );

		data1.addDerivedInto ( data3 );
		data2.addDerivedInto ( data3 );

		data3.addDerivedInto ( data4 );
		
		ch1 = new BioCharacteristicType ( "Organism" );
		ch1.addOntologyTerm ( new OntologyEntry ( "123", new ReferenceSource ( "EFO", null ) ) );
		ch1.addOntologyTerm ( new OntologyEntry ( "456", new ReferenceSource ( "MA", null ) ) );
		cv1 = new BioCharacteristicValue ( "mus-mus", ch1 );
		bm1.addPropertyValue ( cv1 );
		
		ch2 = new BioCharacteristicType ();
		ch2.setTermText ( "Age" );
		cv2 = new BioCharacteristicValue ();
		cv2.setTermText ( "10" );
		cv2.setType ( ch2 );
		timeDim = new UnitDimension ( "time" );
		monthsUnit = new Unit ( "months", timeDim );
		cv2.setUnit ( monthsUnit );
		bm1.addPropertyValue ( cv2 );

		// Cannot be re-used, you need to create a new one, even if it is the same
		cv3 = new BioCharacteristicValue ( "mus-mus", ch1 );
		bm2.addPropertyValue ( cv3 );
		
		cv4 = new BioCharacteristicValue ( "8", ch2 );
		// Units can be recycled instead
		cv4.setUnit ( monthsUnit );
		bm2.addPropertyValue ( cv4 ); 
		
		dp1 = new DataPropertyType ( "p-value" );
		dpv1 = new DataPropertyValue ( "1E-6", dp1 );
		data1.addPropertyValue ( dpv1 );
	}
	
	public void delete ( EntityManager em )
	{
		try
		{
			AccessibleDAO<Product> productDao = new AccessibleDAO<Product> ( Product.class, em );
			
			for ( Field f: this.getClass ().getFields () ) 
			{
				Object o = f.get ( this );
				if ( ! ( o instanceof Product ) ) continue;
				
				Product prod = (Product) o;
				
				Product prodDB = productDao.find ( prod.getAcc () );
				if ( prodDB == null ) continue;
				
				for ( Product up: new HashSet<Product> ( prodDB.getDerivedFrom () ) )
					prodDB.removeDerivedFrom ( up );
				for ( Product down: new HashSet<Product> ( prodDB.getDerivedInto () ) )
					prodDB.removeDerivedInto ( down );
				
				productDao.delete ( (Product) prodDB ); 
			}
		}
		catch (  IllegalArgumentException ex ) {
			throw new RuntimeException ( "Error while deleting the direct-approach test model" );
		}
		catch (  IllegalAccessException ex ) {
			throw new RuntimeException ( "Error while deleting the direct-approach test model" );
		}
	}	
	
}
