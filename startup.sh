#!/bin/bash

RESOURCES_DIR=src/main/resources-development
echo $pwd

cp $RESOURCES_DIR/temp-application-properties.xml $RESOURCES_DIR/application-properties.xml
mvn clean install cargo:run

