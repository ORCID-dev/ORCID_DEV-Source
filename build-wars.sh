#!/usr/bin/env bash
which mvn
current_dir=`pwd`

set -o errexit -o errtrace -o nounset -o functrace -o pipefail
shopt -s inherit_errexit 2>/dev/null || true

trap 'echo "exit_code $? line $LINENO linecallfunc $BASH_COMMAND"' ERR

reset_poms(){
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


}

. ~/work/shellkit/profile.d/shellkit.sh

############################################################
if [[ ! -d ~/.asdf/plugins/mvnd/ ]];then
  asdf plugin-add mvnd https://github.com/joschi/asdf-mvnd
fi

sk-asdf-install-tool-versions
# set JAVA_HOME
. ~/.asdf/plugins/java/set-java-home.bash
_asdf_java_update_java_home
############################################################


tag=${1:-v2.0.1}
tag_numeric=$(echo "$tag" | tr -dc '[:digit:].')

reset_poms

mvn clean
# rm -Rf ~/.m2/repository/org/orcid/orcid-test
rm -Rf ~/.m2/repository/org/orcid

##########

# bump the tagged version in the poms tied to the parent pom
mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false

# bump the tagged version in the poms of projects not tied to the parent pom
mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-test
#mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-utils
#mvn versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-persistence

# install orcid-test into our local maven repo because the builds depend a version tagged release
mvn --projects orcid-test clean install

# install orcid-test into our local maven repo because the builds depend a version tagged release
mvn --projects orcid-utils clean install

# install orcid-parent into our local maven repo because the builds depend a version tagged release
mvn --non-recursive clean install


find ~/.m2/repository/ -name 'orcid*'

sleep 2

mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-message-listener -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-activemq -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-api-web -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-internal-api -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-pub-web -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-scheduler-web -am package -DskipTests
mvnd versions:set -DnewVersion=$tag_numeric -DgenerateBackupPoms=false --projects orcid-web -am package -DskipTests


secs=$SECONDS
hrs=$(( secs/3600 ))
mins=$(( (secs-hrs*3600)/60 ))
secs=$(( secs-hrs*3600-mins*60 ))
printf 'Time spent: %02d:%02d:%02d\n' $hrs $mins $secs | tee /var/tmp/build.log

find . -name '*.war' | tee ~/orcid-source-build.log

find ~/.m2/repository/ -name 'orcid*' | tee ~/orcid-source-build.log
