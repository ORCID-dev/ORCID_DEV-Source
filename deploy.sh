#!/usr/bin/env bash
. ~/.envvars.sh

ARTIFACT_REPO_PATH=/repository/private-release-qa

mvn --batch-mode \
    --settings settings-deploy.xml \
    --file orcid-activemq/pom.xml \
    -Dmaven.test.skip \
    -DaltReleaseDeploymentRepository=github::${ARTIFACT_URL}${ARTIFACT_REPO_PATH} \
    deploy -Dmaven.test.skip

