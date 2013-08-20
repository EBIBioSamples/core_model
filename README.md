  __ The FG Core Object Model __

This is a generic experimental work-flow model, containing basic entities needed for representing biomedical experiment
submissions and experimental work-flows. It was adapted from the existing ArrayExpress 2 model, having in mind the objecting 
of a common umbrella for both AE2 and the BioSample Database repositories.

The project defines both Java classes and their mapping to a relational model, by means of JPA annotations and Hibernate. 

For the moment, there is a separated extension of this project, which realises the [BioSD model](http://github.com/EBIBioSamples/biosd_model).

You find documentation about the BioSD/Core models [here](http://github.com/EBIBioSamples/biosd_model/tree/master/doc).

PLEASE NOTE THIS MODULE IS SHIPPED WITHOUT resources.xml. This is so because this core model is usually extended to
some other model. You should include such Hibernate file in the extension. Probably you'll want to use the [JPA
plug-in](http://github.com/ljnelson/jpa-maven-plugin) to auto-define all the classes from this module to be imported in your extension. 
An example of that can be found in the BioSD model mentioned above.

__ TODO __

* More JUnit tests to be written and run
* Use JAXB to map the model to XML (needed for things like web services)
 
