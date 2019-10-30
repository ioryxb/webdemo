#!/bin/bash
java -classpath hsqldb.jar org.hsqldb.Server -port 9001 -database.0 ./db/mydb -dbname.0 mydb 2>&1