#!/usr/bin/env bash

function check_status() {
	if [ "$1" -gt 0 ]; then
    	  echo "[BUILD FAILED] during $2 phase"
    	  exit $1
	fi
}

cd ./jlv-core && mvn release:clean release:prepare release:perform -B
check_status $? '[jlv-core release]'

cd ../

