spring-boot-metrics-to-influxdb
=================================
This is sample Spring Boot project with metrics with multiple fields in a single line (by using [InfluxDB line protocl](https://docs.influxdata.com/influxdb/v1.7/write_protocols/line_protocol_tutorial/)) exported to InfluxDB.
Document describes, how to run example locally.

*Run InfluxDB*
----------------------
Run locally Docker container with InfluxDB
```bash
docker run -d --name influx -p 8086:8086 influxdb
```

Then login to the InfluxDB shell
```bash
docker exec -it influx influx
```

In the next step create database named `mydb` in which metrics will be stored (this database 
is also set up with spring configuration).
```bash
> create database mydb
````

*Run application*
-----------------
Below are listed possibilities to run application locally.

### run with maven plugin
Just run Spring Boot plugin 
```bash
mvn spring-boot:run
```

### build and run a docker image
Build Docker image of service
```bash
docker build . -t msondecki/spring-boot-influxdb
```
And run Docker container
```bash
docker run -t --name spring-boot-influxd -p 8080:8080 msondecki/spring-boot-influxdb
```

### Sending metrics with line from micrometer
Defined metrics in this project will be grouped by prefix `heap` and sent by micrometer with format of InfluxDB line protocol like following: 
`<measurement>[,<tag-key>=<tag-value>...] <field-key>=<field-value>[,<field2-key>=<field2-value>...] [unix-nano-timestamp]`

Sample of one snapshot of metrics :
```text
heap,metric_type=gauge committed_mb=282,used_mb=45,init_mb=256,max_mb=3641 1571653532165
```
 
*Find your metrics in InfluxDB*
-------------------------------
Login to the InfluxDB shell
```bash
docker exec -it influx influx
```
And select created database
```bash
> use mydb
```
And find all records for selected metric (in our case `heap`):
```bash
> select * from "heap"
```
Then the result can be similar like below:
```sql
name: heap
time                committed_mb init_mb max_mb metric_type used_mb
----                ------------ ------- ------ ----------- -------
1571727337876000000 384          256     3641   gauge       132
1571733374496000000 425          256     3641   gauge       271
1571733434448000000 434          256     3641   gauge       84
```

*Find your metrics in actuator*
-------------------------------
We can read metrics from actuator. Below are listed our metrics
- [heap.committed.mb](http://localhost:8080/actuator/metrics/heap.committed.mb)
- [heap.used.mb](http://localhost:8080/actuator/metrics/heap.used.mb)
- [heap.init.mb](http://localhost:8080/actuator/metrics/heap.init.mb)
- [heap.max.mb](http://localhost:8080/actuator/metrics/heap.max.mb)

Then the result for [heap.committed.mb](http://localhost:8080/actuator/metrics/heap.committed.mb) can be like below:
```json
{"name":"heap.committed.mb","description":null,"baseUnit":null,"measurements":[{"statistic":"VALUE","value":434.0}],"availableTags":[]}
```