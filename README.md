# Sample Code SMPP Protocol #

## Description ##

Sample code for learning SMPP Protocol with [OpenSMPP](http://opensmpp.org/) library.

## Features ##

### Client Application ###

* Send SMPP Message
* Process Delivery Report

### Server Application ###

* Receive SMPP Message
* Send Delivery Report

## Run ##

### Server Application ###

```
mvn clean package exec:java -Dexec.mainClass=com.muhardin.belajar.smpp.server.Main
```

### Client Application ###


```
mvn clean package exec:java -Dexec.mainClass=com.muhardin.belajar.smpp.client.Main
```
