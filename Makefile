build:
	mvn install

run:
	POD_NAME="$(shell sudo kubectl get pods -l "name=postgres-airflow" -o jsonpath="{.items[0].metadata.name}")"; sudo kubectl cp create_transaction_table.sql "$$POD_NAME":/
	POD_NAME="$(shell sudo kubectl get pods -l "name=postgres-airflow" -o jsonpath="{.items[0].metadata.name}")"; sudo kubectl exec -it $$POD_NAME psql airflow < create_transaction_table.sql
	mvn exec:java

build_spark:
	sudo docker build \
    --build-arg SPARK_VERSION_KEY=spark-1.6.3-bin-hadoop2.6 \
    -t spark-2.1.0-bin-hadoop2.6 ./spark

run_spark:
	sudo kubectl apply -f spark/spark.yaml

get_pods:
	sudo kubectl get pods -n stream

generate:
	python kafka/kafka_producer.py localhost:30092 test

source_topic:
	POD_NAME="$(shell sudo kubectl get pods -l "app=kafka" -o jsonpath="{.items[0].metadata.name}" -n stream)"; sudo kubectl exec -it $$POD_NAME -n stream -- bin/kafka-console-consumer.sh --bootstrap-server $$POD_NAME:9092 --topic test --from-beginning

target_topic:
	POD_NAME="$(shell sudo kubectl get pods -l "app=kafka" -o jsonpath="{.items[0].metadata.name}" -n stream)"; sudo kubectl exec -it $$POD_NAME -n stream -- bin/kafka-console-consumer.sh --bootstrap-server $$POD_NAME:9092 --topic target --from-beginning 
