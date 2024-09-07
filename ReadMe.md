To run the producer you need to run this following command:

```bash
cd /usercode/project/producer-async && mvn package && java -jar target/kafka-producer-async-task.jar
```

To run the consumer to consume the records created by the producer you need to run this command:

```bash
cd /usercode/project/consumer && mvn package && java -jar target/kafka-consumer-task.jar
```