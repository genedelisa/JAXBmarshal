# JAXB parent-child cycle problem

Marshalling an object graph in a web service via Spring @MVC 3 is a piece of cake.

But you need to be careful when you marshal an object graph that contains relationships such as @OneToMany and @ManyToOne.

For the complete discussion, read the rest of my original [blog post](http://rockhoppertech.com/blog/jaxb-parent-child-cycle-problem/).

## Usage

    mvn jetty:run
    curl -i -H "Accept: application/xml" http://localhost:8080/jaxbmarshal/composerws/Mozart/Wolfgang/Amadeus
	curl -i -H "Accept: application/json" http://localhost:8080/jaxbmarshal/composerws/composers
    

## License

Copyright (C) 2011 Gene De Lisa

Distributed under the Eclipse Public License.
