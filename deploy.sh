#!/usr/bin/env bash
. ~/.envvars.sh

ARTIFACT_REPO_PATH=/repository/private-release-qa

set -o errexit -o errtrace -o nounset -o functrace -o pipefail
shopt -s inherit_errexit 2>/dev/null || true

trap 'echo "exit_code $? line $LINENO linecallfunc $BASH_COMMAND"' ERR

for service in orcid-message-listener orcid-activemq orcid-api-web orcid-internal-api orcid-pub-web orcid-scheduler-web orcid-web;do

  mvn --batch-mode \
      --settings settings-deploy.xml \
      --file "${service}/pom.xml" \
      -Dmaven.test.skip \
      -DaltReleaseDeploymentRepository=github::${ARTIFACT_URL}${ARTIFACT_REPO_PATH} \
      deploy -Dmaven.test.skip
done

