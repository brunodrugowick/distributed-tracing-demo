#!/bin/bash
echo 'Starting zipkin server...'
java -jar ../zipkin/zipkin-server-2.21.5-exec.jar >> zipkin.log &
echo 'Starting Prometheus...'
cd ../prometheus/ && ./prometheus >> prometheus.log &
echo 'Starting services...'
cd ../service-one/ && ./mvnw spring-boot:run >> service-one.log &
cd ../service-two/ && ./mvnw spring-boot:run >> service-two.log &
cd ../service-three/ && ./mvnw spring-boot:run >> service-three.log &
cd ../service-four/ && ./mvnw spring-boot:run >> service-four.log &
echo 'Waiting 20s for services to start'
sleep 20 
echo 'Starting benchmarking...'
ab -c 10 -n 10000 http://localhost:8080/info
echo 'Go to http://localhost:9411/ to see trace information.'
echo 'Go to http://localhost:9090/ to observe the services via Prometheus.'