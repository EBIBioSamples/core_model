* The FG Core Object Model * 

This is a generic experimental work-flow model, containing basic entities needed for representing biomedical experiment
submissions and experimental work-flows. It was adapted from the existing ArrayExpress 2 model, having in mind the objecting 
of a common umbrella for both AE2 and the BioSample Database repositories.

The project defines both Java classes and their mapping to a relational model, by means of JPA annotations and Hibernate. 

For the moment, there is a separated extension of this project (TODO: link), which relaises the BioSD model.

** TODO ** 

* JUnit tests to be written and run
* Many DAOs
* Complete ORM (database indexes)
* Use JAXB to map the model to XML (needed for things like web services)
* Utilities (e.g., graph-walking, node searching, node's attribute searching)
 