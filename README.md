# Kafka Spark Streaming
Event streaming simple example

## Install Streaming-Setup
* Follow instructions for [streaming example](https://github.com/marodeur100/talend_kub_airflow/tree/master/stream-example) 

## Install Spark
* Install Spark
```shell
cd /opt && \
    curl http://www.us.apache.org/dist/spark/spark-2.4.0/spark-2.4.0-bin-hadoop2.6.tgz | \
        sudo tar -zx && \
    sudo ln -s spark-2.4.0-bin-hadoop2.6 spark && \
    echo Spark 2.4.0 installed in /opt
export SPARK_HOME=/opt/spark
export PATH=$SPARK_HOME/bin:$PATH
```
## Install Python and Kafka
* Install pip 
```
sudo apt-get install python-pip
pip install kafka
```

## Start Streamdata Generator
* The generator sends dummy test data to the topic test
```shell
make generate
```

## Run
* Open new terminal
* if needed adjust pom.xml with arguments: kafka_broker:port source_topic target_topic postgreshost db_user db_pwd
```shell
make build
make run
```
## Check Elastic Search
* Install elsaticserach-head plugin for firefox browser
* Open plugin and connect with localhost:30192
* The index customer_transactions should appear
