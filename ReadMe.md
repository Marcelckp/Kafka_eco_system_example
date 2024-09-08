To run the producer you need to run this following command:

```bash
cd /usercode/project/producer-async && mvn package && java -jar target/kafka-producer-async-task.jar
```

To run the consumer to consume the records created by the producer you need to run this command:

```bash
cd /usercode/project/consumer && mvn package && java -jar target/kafka-consumer-task.jar
```

### Stateless 

If you want to produce values in the console to a topic we need to do the following. Firs we need to create the topic:

```bash
# Input topic
/app/confluent-7.3.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic input-topic --partitions 1 --replication-factor 1

# Output topic
/app/confluent-7.3.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic output-topic --partitions 1 --replication-factor 1
```

Then we need to run our stream mutation application:

```bash
cd /usercode/project/kafka-streams-stateless-ops && mvn package && java -jar target/kafka-streams-stateless-ops-task.jar
```

And then we need to run our console producer application that will supply the console messages to the consumer topic:

```bash
/app/confluent-7.3.1/bin/kafka-console-producer --broker-list localhost:9092 --topic input-topic --property "parse.key=true" --property "key.separator=:"
```

Now that our services are up we should see the console app have an input indicator `>`. If this is present we can add messages to pass to our stream enrichment/mutation stream builder application that is listening on the input-topic. The messages should look as follow:

```bash
user42:michael scofield
user1:theodore bagwell
user400:sarah tancredi
user99:lincoln burrows
```

If we want to view the output as it is coming through we need to open up a view into our output-topic to view the enriched/mutated produced console messages coming in.

### StateFul

We can create a count application that will count and keep track of the keys that have come through the stream enrichment/mutation stream and pushed to a new output topic. 

We will need to delete and recreate the topics to flush the data within the current topics.

Once that is done we need to run our stateful kafka interceptor application:

```bash
cd /usercode/project/kafka-streams-stateful-ops && mvn package && java -jar target/kafka-streams-stateful-ops-task.jar
```

If we use the console producer application and view the output-topic again we can see that we will begin to count the keys of the messages we push through our data pipeline. Use these as example messages:

```bash
app_1:500
app_1:404
app_2:403
app_2:403
app_2:429
```

<br />

### Using kafka within JAVA Spring

We need to create the new topic for our Spring example, below is the command to do so:

```bash
/app/confluent-7.3.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic spring-kafka-demo-topic --partitions 1 --replication-factor 1
```

Once this is up we can build and run our spring server application and run our console consumer application to view the output in the spring kafka demo topic.

```bash
# Build
cd /usercode/project/spring-kafka-producer/ && mvn package && java -jar target/spring-kafka-producer-0.0.1-SNAPSHOT.jar

# Open consumer
/app/confluent-7.3.1/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic spring-kafka-demo-topic --from-beginning
```

Once these are both up we can query our Spring server and see the data being passed through the event pipeline.

```bash
curl -i -X POST -d 'redis=keyvalue' http://localhost:8080/send

curl -i -X POST -d 'postgres=rdbms' http://localhost:8080/send

curl -i -X POST -d 'cassandra=column' http://localhost:8080/send
```

Our spring server is running on port 8080 on our local machine and will use the send EP to push the data we send to the spring EP `-d dataToSend=THEDATA` through to the kafka consumer with a producer and from there we can manipulate the data and enrich it within our data pipeline and create ETL jobs etc.

We can create a consumer within spring as well. If we which to run the Spring consumer we will need to use the following command:

```bash
cd /usercode/project/spring-kafka-consumer/ && mvn package && java -jar target/spring-kafka-consumer-0.0.1-SNAPSHOT.jar
```

Now we can make requests to our send message endpoint to produce the messages again and we should see that our consumer is listening for the messages and that we are mutating them correctly before printing them to the console.