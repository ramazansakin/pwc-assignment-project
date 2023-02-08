#!/bin/bash

# Build the project
mvn clean install

# Run the application
java -jar target/assignment-pwc-0.0.1-SNAPSHOT.jar