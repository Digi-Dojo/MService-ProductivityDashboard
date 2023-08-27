#!/bin/bash

java -Dspring.datasource.username=${SPRINGDBUSER} -Dspring.datasource.password=${SPRINGDBPW} -Dspring.datasource.url=jdbc:${SPRINGDBURL} -jar app.jar

