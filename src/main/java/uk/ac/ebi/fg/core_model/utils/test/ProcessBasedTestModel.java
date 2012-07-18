package uk.ac.ebi.fg.core_model.utils.test;

import uk.ac.ebi.fg.core_model.expgraph.BioMaterial;
import uk.ac.ebi.fg.core_model.expgraph.BioMaterialProcessing;
import uk.ac.ebi.fg.core_model.expgraph.Data;
import uk.ac.ebi.fg.core_model.expgraph.DataAcquisition;
import uk.ac.ebi.fg.core_model.expgraph.DataProcessing;
import uk.ac.ebi.fg.core_model.expgraph.properties.BioCharacteristicType;
import uk.ac.ebi.fg.core_model.expgraph.properties.BioCharacteristicValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.DataPropertyType;
import uk.ac.ebi.fg.core_model.expgraph.properties.DataPropertyValue;
import uk.ac.ebi.fg.core_model.expgraph.properties.Unit;
import uk.ac.ebi.fg.core_model.expgraph.properties.UnitDimension;
import uk.ac.ebi.fg.core_model.terms.OntologyEntry;
import uk.ac.ebi.fg.core_model.xref.ReferenceSource;

/**
 * 
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>Jul 12, 2012</dd></dl>
 * @author Marco Brandizi
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class ProcessBasedTestModel
{
	public BioMaterial bm1;
	public BioMaterial bm2;
	public BioMaterialProcessing proc1;
	public BioMaterial bm3;
	public DataAcquisition proc2;
	public Data data1;
	public Data data2;
	public DataProcessing proc3;
	public Data data3;
	public Data data4;
	private BioCharacteristicType ch1;
	private BioCharacteristicValue cv1;
	private BioCharacteristicType ch2;
	private BioCharacteristicValue cv2;
	private BioCharacteristicValue cv3;
	private BioCharacteristicValue cv4;
	private UnitDimension timeDim;
	private Unit monthsUnit;
	private DataPropertyType dp1;
	private DataPropertyValue dpv1;

	public ProcessBasedTestModel () {
		this ( "" );
	}

	/**
	 *  <pre>
	 *  bm1 ----> proc1 ---> bm3 ---> proc2 ---> data1 ---> proc3 ---> data3
	 *  bm2 ---/                           \---> data2 --/        \--> data4
	 *  </pre>        
	 */
	public ProcessBasedTestModel ( String prefix )
	{
		bm1 = new BioMaterial ( prefix + "bm1" );
		bm2 = new BioMaterial ( prefix + "bm2" );
		// You can use the generic one, but the specific processings are better for validation purposes
		proc1 = new BioMaterialProcessing ( prefix + "proc1" );
		bm3 = new BioMaterial ( prefix + "bm3" );
		proc2 = new DataAcquisition ( prefix + "proc2" );
		data1 = new Data ( prefix + "data1" );
		data2 = new Data ( prefix + "data2" );
		proc3 = new DataProcessing ( prefix + "proc3" );
		data3 = new Data ( prefix + "data3" );
		data4 = new Data ( prefix + "data4" );
		
		bm1.addDownstreamProcess ( proc1 );
		// for Products, This is just an alias of downStreamProcess (but not in Hibernate, do not use it in HQL)
		// The parameter type is constrained to the right type of node, if you used the right type of processing
		bm2.addDownstreamNode ( proc1 );
		bm3.addUpstreamNode ( proc1 );
		proc2.addInput ( bm3 );
		// Again, this is an alias of data for this kind of node
		proc2.addDownstreamNode ( data1 );
		data2.addUpstreamProcess ( proc2 );
		data1.addDownstreamProcess ( proc3 );
		// This doesn't change anything, it's the simmetric operation of the previous one
		proc3.addUpstreamNode ( data1 );
		proc3.addOutput ( data3 );
		data4.addUpstreamNode ( proc3 );
		
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
	
}
