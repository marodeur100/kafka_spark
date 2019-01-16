package com.accenture.streaming;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.json_tuple;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Spark Structured Stream Processing with Apache Kafka <br>
 * Consumes JSON data from Kafka, maps the data into Java Objects and joins the
 * Kafka data with static data from a CSV file.
 *
 */
/**
 * @author felix.klemm
 *
 */
public class App {
	static final String KAFKA_SUBSCRIBE_TYPE = "subscribe";
	static final String SPARK_MASTER = "spark.master";
	static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";

	public static void main(String[] args) throws StreamingQueryException {

		// input parameters parsing
		if (args.length < 4) {
			System.err.println(
					"Required input params -> <bootstrap-server (brokerhost:port)> <source_topic> <target_topic> <spark_master> <elastichost>");
			System.exit(1);
		}

		// Kafka Broker IP with port
		String bootstrapServers = args[0];
		// Kafka Source Topic
		String topics = args[1];
		// Kafka Target Topic
		String targetTopic = args[2];
		// Spark Master address
		String masterAddress = args[3];
		// postgres address
		String postgresUrl = args[4];
		// postgres user credentials
		String postgresUser = args[5];
		String postgresPassword = args[6];
		
		String url = postgresUrl;
		Properties props = new Properties();
		props.setProperty("user",postgresUser);
		props.setProperty("password",postgresPassword);
		props.setProperty("ssl","false");

		
		// Getting the static CSV data from a directory
		SparkSession spark = SparkSession.builder().appName("Kafka Streaming Example")
				.config(SPARK_MASTER, masterAddress).getOrCreate();

		// mute it down, Spark is superchatty on INFO
		spark.sparkContext().setLogLevel("WARN");

		
		// now let's read the CSV and associate it with our staticSchema
		Dataset<Row> staticData = spark.read().jdbc(postgresUrl, "customer_nf", props);
		
		// Definition of the Kafka Stream including the mapping of JSON into Java
		// Objects
		Dataset<UserActivity> kafkaEntries = spark.readStream() // read a stream
				.format("kafka") // from KAFKA
				.option("kafka.bootstrap.servers", bootstrapServers) // connection to servers
				.option("subscribe", topics).load() // subscribe & load
				.select(json_tuple(col("value").cast("string"), // explode value column as JSON
						"action", "id", "username", "ts")) // JSON fields we extract
				.toDF("action", "uid", "username", "ts") // map columns to new names
																							// (same here in demo)
				.as(Encoders.bean(UserActivity.class)); // make a good old JavaBean out of it

		// Join kafkaEntries with the static data
		Dataset<Row> joinedData = kafkaEntries.join(staticData, "uid");

		// Write the real-time data from Kafka to the console
		StreamingQuery query = kafkaEntries.writeStream() // write a stream
				.trigger(Trigger.ProcessingTime(2000)) // every two seconds
				.format("console") // to the console
				.outputMode(OutputMode.Append()) // only write newly matched stuff
				.start();

		// block main thread until done.
		query.awaitTermination();
	}
}
