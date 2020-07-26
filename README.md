# Distributed Tracing Demo

This project was develop with the purpose of testing and understanding Spring Cloud Sleuth and Spring Cloud Zipkin.

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

