bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.

## Getting Started

You need Java 8 on your environment. Project can be installed with maven and developed in Intellij IDEA.

Services use embedded H2 DB. On every start schema is regenerated.

Test coverage is 90% but there are still lots of scenarios missing. 

I made an assumption kill means killing a hero by another hero, so I only filtered that cases. I left alternate property commented out on CombatLogServiceImpl class.

### Running service

You can navigate to the projects' directory and run the commands below

```
mvn spring-boot:run
```

### Installing and Packaging

On project root, you need to run this command to be able to build the project (on Unix shell):

```
mvn clean install
```

This command runs the tests and generates a runnable fat jar file.

### API documentation

Swagger documentation can be accessed using and testing of related endpoints:

```
http://localhost:8080/swagger-ui.html

```
