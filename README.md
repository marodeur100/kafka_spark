# Kafka Spark Streaming
* requires running Kafka
* requires running postgresDB
* requires Spark installation

## Run
* adjust pom.xml with arguments: kafka_broker:port source_topic target_topic postgreshost db_user db_pwd
* mvn install
* mvn exec:java
