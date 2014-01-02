#!/usr/bin/env bash

echo '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[JLV-CORE BUILD]'
cd ../jlv-core && mvn clean install
status=$?

if [ "$status" -gt 0 ]
then
    echo '[BUILD FAILED]'
    exit $status
fi

echo '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[JLV-PLUGIN BUILD]'
cd ../jlv-eclipse-plugin/jlv-plugin && mvn clean install -DskipTests
status=$?

if [ "$status" -gt 0 ]
then
    echo '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[BUILD FAILED]'
else
    echo '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[BUILD SUCCESS]'
fi

exit $status
