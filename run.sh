# Script for running rookeeper and kafka for our project
/app/confluent-7.3.1/bin/zookeeper-server-start /app/confluent-7.3.1/etc/kafka/zookeeper.properties >/dev/null& 
echo "zookeeper started" && sleep 2s

/app/confluent-7.3.1/bin/kafka-server-start /app/confluent-7.3.1/etc/kafka/server.properties >/dev/null&
echo "kafka started" && sleep 5s