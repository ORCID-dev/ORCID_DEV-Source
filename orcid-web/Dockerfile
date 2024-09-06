# build stage

# match version from .tool-versions
FROM maven:3.6.3-jdk-11 AS maven

WORKDIR /build

# copy all the git repo to build dir
COPY . .

RUN mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-web -am package -DskipTests

###########################################################

# For Java 11

FROM tomcat:jre11-temurin-jammy

# copy jar file from build
COPY --from=maven /build/target/*.war /usr/local/tomcat/webapps/ROOT.war

#ADD newrelic/newrelic.yml /opt/app/newrelic.yml
#RUN curl -L -s https://download.newrelic.com/newrelic/java-agent/newrelic-agent/7.6.0/newrelic-agent-7.6.0.jar -o /opt/app/newrelic.jar

# add orcid ca to allow Java application to trust other containers
#ADD cacerts /opt/java/openjdk/lib/security/cacerts

# add orcid ca to system to allow curl healthchecks to work
#ADD ca-certificates.crt /etc/ssl/certs/ca-certificates.crt

# Build a shell script because the ENTRYPOINT command doesn't like using ENV
#RUN echo "#!/usr/bin/env bash \n java \$JAVA_OPTS -javaagent:/opt/app/newrelic.jar -jar gateway.jar" > ./entrypoint.sh

#RUN chmod +x ./entrypoint.sh

#ENTRYPOINT ./entrypoint.sh

