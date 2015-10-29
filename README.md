  __ The FG Core Object Model __

This is a generic experimental work-flow model, containing basic entities needed for representing biomedical experiment
submissions and experimental work-flows. It was adapted from the existing ArrayExpress 2 model, having in mind the objecting 
of a common umbrella for both AE2 and the BioSample Database repositories.

The project defines both Java classes and their mapping to a relational model, by means of JPA annotations and Hibernate. 

For the moment, there is a separated extension of this project, which realises the [BioSD model](http://github.com/EBIBioSamples/biosd_model).

You find documentation about the BioSD/Core models [here](http://github.com/EBIBioSamples/biosd_model/tree/master/doc).

PLEASE NOTE THE BINARY FOR THIS MODULE IS SHIPPED WITHOUT hibernate.properties. This is so because this core model is usually extended to some other model. You should include such Hibernate file in the extension. See [Resources](/EBIBioSamples/core_model/blob/master/src/main/java/uk/ac/ebi/fg/core_model/resources/Resources.java) to have an idea on how you can include your own classes into the ones that Hibernate detects for ORM (no persistence.xml is needed). An example of that can be found in the BioSD model mentioned above.

__ TODO __

* More JUnit tests to be written and run
* Use JAXB to map the model to XML (needed for things like web services)
 
