# Distributed Tracing Demo

This project was develop with the purpose of testing and understanding Spring Cloud Sleuth and Spring Cloud Zipkin.

This is an analysis of a trace (multiple requests spamming  several services):
![https://i.imgur.com/YlU2bK1.png](https://i.imgur.com/YlU2bK1.png)

This is the visualization of service dependencies:
![https://i.imgur.com/qZt0Pym.png](https://i.imgur.com/qZt0Pym.png)

## Sleuth...

... marks logs with information that will be used for distributed tracing later.

Example of a log line with Sleuth:

```
2020-07-25 23:43:36.021  INFO [service-two,838f7c8a473d02df,f7e7a5468a2f7526,true] 789 --- [nio-8081-exec-4] d.d.d.servicetwo.MyController            : returning ok from Service Two
```

Notice the `[service-two,838f7c8a473d02df,f7e7a5468a2f7526,true]` part. This is what Sleuth adds.

## Zipkin...

... is an application that receives part of the log info (you can configure) and is able to show timing information about the requests through multiple (micro)services as well as a graphical visualization of your services infrastructure.

# Running

This demo is composed of 4 services. The first one ("first layer") balances requests between 2 and 3 (the "second layer" of services) which in turn ends up requesting info to the service number 4 (the "third layer" of the infrastructure).

Service 3 has a `Thread.sleep(200)` to make it slower. You can see on Zipkin that he gets less requests than service 2 b ecause  of that.

You may run the `scripts/start-all.sh` script, which starts everything and runs an `ab` (apache Bench) test for you to have some info on Zipkin server. After running it instructs you to head to `http://localhost:9411` and analyze the tracing information for yourself.

```
NOTE: you need java 11 and Apache Bench for the script to work properly.
```

```
NOTE: you may have to stop the services manually later. Improvements on the script to come, maybe.
```

Below  is the execution log:

```bash
$ ./start-all.sh 
Starting zipkin server...
Starting services...
Waiting 20s for services to start
Starting benchmarking...
This is ApacheBench, Version 2.3 <$Revision: 1843412 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /info
Document Length:        89 bytes

Concurrency Level:      10
Time taken for tests:   159.370 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      1940000 bytes
HTML transferred:       890000 bytes
Requests per second:    62.75 [#/sec] (mean)
Time per request:       159.370 [ms] (mean)
Time per request:       15.937 [ms] (mean, across all concurrent requests)
Transfer rate:          11.89 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       4
Processing:     2  158 150.5    302     744
Waiting:        2  158 150.5    302     733
Total:          2  159 150.5    302     744

Percentage of the requests served within a certain time (ms)
  50%    302
  66%    308
  75%    308
  80%    309
  90%    310
  95%    313
  98%    317
  99%    321
 100%    744 (longest request)
Go to http://localhost:9411/ to see trace information
```

And the resultant Zipkin graph ([http://localhost:9411/zipkin/dependency](http://localhost:9411/zipkin/dependency)):

![https://i.imgur.com/dTyEScc.png](https://i.imgur.com/dTyEScc.png)
