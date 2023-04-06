#!/usr/bin/env bash
which mvn
current_dir=`pwd`

set -o errexit -o errtrace -o nounset -o functrace -o pipefail
shopt -s inherit_errexit 2>/dev/null || true

trap 'echo "exit_code $? line $LINENO linecallfunc $BASH_COMMAND"' ERR


clean(){

  git checkout orcid-activemq/pom.xml
  git checkout orcid-api-common/pom.xml
  git checkout orcid-api-web/pom.xml
  git checkout orcid-core/pom.xml
  git checkout orcid-internal-api/pom.xml
  git checkout orcid-message-listener/pom.xml
  git checkout orcid-persistence/pom.xml
  git checkout orcid-pub-web/pom.xml
  git checkout orcid-scheduler-web/pom.xml
  git checkout orcid-test/pom.xml
  git checkout orcid-utils/pom.xml
  git checkout orcid-web/pom.xml
  git checkout pom.xml

  mvn clean

  rm -Rf ~/.m2/repository/org/orcid/orcid-model
  rm -Rf ~/.m2/repository/org/orcid/orcid-test

}

. ~/work/shellkit/profile.d/shellkit.sh

sk-asdf-install-tool-versions

tag=${1:-release-2.0.1}

clean

##########

# install orcid-test into our local maven repo because the builds depend a version tagged release
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-test clean install


mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-message-listener -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-activemq -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-api-web -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-internal-api -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-pub-web -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-scheduler-web -am package -DskipTests
mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-web -am package -DskipTests


secs=$SECONDS
hrs=$(( secs/3600 ))
mins=$(( (secs-hrs*3600)/60 ))
secs=$(( secs-hrs*3600-mins*60 ))
printf 'Time spent: %02d:%02d:%02d\n' $hrs $mins $secs | tee /var/tmp/build.log

find . -name '*.war' | tee /var/tmp/build.log

