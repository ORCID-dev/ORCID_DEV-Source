#!/usr/bin/env bash

cd /usr/local/tomcat

# template any properties files
for j2_file in *.j2;do
  j2 $j2_file -o $(basename $j2_file .j2) --undefined
done

/usr/local/tomcat/bin/catalina.sh run
