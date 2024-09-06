# dependencies docker build

# match version from .tool-versions
FROM maven:3.6.3-jdk-11 AS maven

ARG tag_numeric

WORKDIR /build

COPY pom.xml .
COPY orcid-scheduler-web/pom.xml orcid-scheduler-web/pom.xml
COPY orcid-api-web/pom.xml orcid-api-web/pom.xml
COPY orcid-persistence/pom.xml orcid-persistence/pom.xml
COPY orcid-web-frontend/pom.xml orcid-web-frontend/pom.xml
COPY orcid-activities-indexer/pom.xml orcid-activities-indexer/pom.xml
COPY orcid-utils/pom.xml orcid-utils/pom.xml
COPY orcid-message-listener/pom.xml orcid-message-listener/pom.xml
COPY orcid-core/pom.xml orcid-core/pom.xml
COPY orcid-web/pom.xml orcid-web/pom.xml
COPY orcid-internal-api/pom.xml orcid-internal-api/pom.xml
COPY orcid-test/pom.xml orcid-test/pom.xml
COPY orcid-pub-web/pom.xml orcid-pub-web/pom.xml
COPY orcid-activemq/pom.xml orcid-activemq/pom.xml
COPY orcid-api-common/pom.xml orcid-api-common/pom.xml
COPY orcid-nodejs/pom.xml orcid-nodejs/pom.xml

# download maven dependencies and ignore that some components will fail
RUN mvn -T 1C --batch-mode dependency:resolve --fail-never

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

