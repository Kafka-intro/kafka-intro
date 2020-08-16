# Kafka into

### Confluent kafka in docker 
This repository contains modified confluent kafka compose.
Once you have docker installed, use following commands to: 

``docker-compose up  --build`` - to spin up local kafka cluster

``CTRL + C`` - press to stop it

``docker-compose down`` - when stopped, run it to delete all topics and data 

http://localhost:9021/clusters - control center UI

See [details here](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html#ce-docker-quickstart)

### UI cars-producer
There is an app with UI and cars which produce data to carsInfo (JSON data with car positions) and carsErrors (JSON data with info about cars hitting ) topics.  
Use keyboard arrows to control first car, and A/W/S/D to control second car. 
See details app details in cars-procucer-with-ui.

http://localhost:5000/ - cars-producers UI 

### Console producer and consumer

#### Producer
``docker exec -it broker kafka-console-producer --bootstrap-server localhost:9092 --property "parse.key=true"  --property "key.separator=:" --topic <TOPIC_NAME>``  

#### Consumer
``docker exec broker kafka-console-consumer --bootstrap-server localhost:9092 -property print.key=true --topic <TOPIC_NAME>``

### Java producer and consumer
See examples in kafka intro -> com.awesome.solutions package

### How to learn kafka 
[Udemy](https://www.udemy.com/) courses by Stephane Marek's. Start with [this one](https://www.udemy.com/course/apache-kafka/).

Have fun! 


