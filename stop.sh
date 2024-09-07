#!/bin/bash

# Stop Kafka
/app/confluent-7.3.1/bin/kafka-server-stop
echo "kafka stopped"

# Give it some time to ensure Kafka is fully stopped
sleep 2s

# Stop Zookeeper
/app/confluent-7.3.1/bin/zookeeper-server-stop
echo "zookeeper stopped"
