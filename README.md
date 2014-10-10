Spring Webapp Archetype
=======================
Simple Maven archetype for web application project template with main technologies like Spring, Hibernate and JSF.

To use this archetype, first clone this project to your local workstation. Then install it to your local maven repository with command:

	mvn clean install

After successful installation you can create project from archetype with this command:

	mvn archetype:generate -DarchetypeGroupId=com.github.chosema -DarchetypeArtifactId=spring-webapp-archetype -DarchetypeVersion=<current_version> -DgroupId=<your_project_groupId> -DartifactId=<your_project_artifactId>