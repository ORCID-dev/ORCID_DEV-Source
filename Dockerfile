# dependencies docker build

# match version from .tool-versions
FROM maven:3.6.3-jdk-11 AS maven

WORKDIR /build

# copy all the git repo to build dir
COPY . .

# bump the tagged version in the poms tied to the parent pom
RUN mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false

# bump the tagged version in the poms of projects not tied to the parent pom
RUN mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-test

# install orcid-parent into our local maven repo because the builds depend a version tagged release
RUN mvn --non-recursive clean install -DskipTests

# install orcid-test into our local maven repo because the builds depend a version tagged release
RUN mvn --projects orcid-test clean install -DskipTests

# install orcid-utils into our local maven repo because the builds depend a version tagged release
RUN mvn --projects orcid-utils clean install -DskipTests

# install orcid-persistence into our local maven repo because orcid-core depends on it
RUN mvn --projects orcid-persistence clean install -DskipTests

# install orcid-core into our local maven repo because the builds depend a version tagged release
RUN mvn --projects orcid-core clean install -DskipTests

# install orcid-api-common into our local maven repo because orcid-web deploy depends a version tagged release
RUN mvn --projects orcid-api-common clean install -DskipTests

