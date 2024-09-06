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

# # copy all the git repo to build dir
# COPY . .
# 
# # bump the tagged version in the poms tied to the parent pom
# RUN mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false
# 
# # bump the tagged version in the poms of projects not tied to the parent pom
# RUN mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-test
# 
# # install orcid-parent into our local maven repo because the builds depend a version tagged release
# RUN mvn --non-recursive clean install -DskipTests
# 
# # install orcid-test into our local maven repo because the builds depend a version tagged release
# RUN mvn --projects orcid-test clean install -DskipTests
# 
# # install orcid-utils into our local maven repo because the builds depend a version tagged release
# RUN mvn --projects orcid-utils clean install -DskipTests
# 
# # install orcid-persistence into our local maven repo because orcid-core depends on it
# RUN mvn --projects orcid-persistence clean install -DskipTests
# 
# # install orcid-core into our local maven repo because the builds depend a version tagged release
# RUN mvn --projects orcid-core clean install -DskipTests
# 
# # install orcid-api-common into our local maven repo because orcid-web deploy depends a version tagged release
# RUN mvn --projects orcid-api-common clean install -DskipTests
# 
# 
# 

