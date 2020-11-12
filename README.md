# netty-server

#Description
A simple Netty Server application example. This can be played with the project `netty-client` for a demo on how Netty works.

## Prereq
* Maven

## Notes
Server runs on port 8066, but can be modified in `application.properties`

## Running the server
This can be run from any IDE as a simple Java project or the following commands can be used for running from CLI
* `mvn clean compile`
* `mvn clean install`
* `java -jar target/netty-server-1.0.0.jar`

## Logs
Logs are pushed to the `/tmp/netty` directory and can be modified in `resources/logback.xml` file.
To view logs, use the following command
* `tail -f /tmp/netty/netty-server.log`