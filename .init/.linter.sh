#!/bin/bash
cd /home/kavia/workspace/code-generation/quality-defect-management-system-241676-241687/backend_api
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

