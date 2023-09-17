#!/bin/bash

java \
  -Dspring.datasource.username="${SPRINGDBUSER}" \
  -Dspring.datasource.password="${SPRINGDBPW}" \
  -Dspring.datasource.url=jdbc:"${SPRINGDBURL}" \
  -Dspring.kafka.properties.bootstrap.servers="${KAFKABOOTSTRAPSERVERS}" \
  -Dspring.kafka.properties.sasl.jaas.config="${KAFKAJAAS}" \
  -Dspring.kafka.properties.basic.auth.user.info="${KAFKAUSERINFO}" \
  -Dspring.kafka.properties.schema.registry.url="${SPRINGREGISTRYURL}" \
  -jar app.jar

