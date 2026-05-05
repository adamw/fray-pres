#!/usr/bin/env bash
set -euo pipefail

if [ $# -ne 1 ]; then
    echo "Usage: $0 <test-number>" >&2
    exit 1
fi

exec mvn test -Dtest="*$1*" -Dsurefire.failIfNoSpecifiedTests=false
