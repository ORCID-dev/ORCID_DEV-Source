#!/usr/bin/env bash
. ~/.envvars.sh

ARTIFACT_REPO_PATH=/repository/private-release-qa

tag=${1:-release-2.0.1}

mvn versions:set -DnewVersion=$tag -DgenerateBackupPoms=false --projects orcid-test clean install

mvn --batch-mode \
    --settings settings-deploy.xml \
    --file orcid-activemq/pom.xml \
    -Dmaven.test.skip \
    -DaltReleaseDeploymentRepository=github::${ARTIFACT_URL}${ARTIFACT_REPO_PATH} \
    deploy -Dmaven.test.skip

