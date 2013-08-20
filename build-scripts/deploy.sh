#!/usr/bin/env bash

# $1 - release version for jlv eclipse plugin
# $2 - next snapshot version
# E.g.: ./deploy.sh 1.0.0 1.0.1

function check_status() {
	if [ "$1" -gt 0 ]
	then
    	echo '[BUILD FAILED]'
    	exit $1
	fi
}

echo '[JLV BUILD]'
cd ../jlv && mvn clean install
check_status $?

echo '[JLV-ECLIPSE-PLUGIN BUILD]'
echo "Bumping jlv eclipse plugin version to $1"
cd ../jlv-eclipse-plugin && mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=$1
check_status $?

echo 'Building jlv-eclipse-plugin project'
mvn clean install
check_status $?

echo '[JLV-ECLIPSE-PLUGIN DEPLOY]'
echo 'Commiting changes'
cd ..
git add .
git commit -m "jlv eclipse plugin version was updated to $1"
check_status $?

echo 'Switching to repo branch'
git checkout repo
check_status $?

echo 'Removing old jars'
cd eclipse && rm -fr *.jar features plugins
check_status $?

echo 'Copying new jars into eclipse/'
cd .. && cp -R jlv-eclipse-plugin/jlv-update-site/target/repository/* eclipse/
check_status $?
cp jlv-eclipse-plugin/jlv-update-site/target/*.zip eclipse/archive/
check_status $?

echo 'Commiting and pushing changes'
git add -A eclipse
git commit -m "JLV released (v.$1)"
git push
check_status $?

echo 'Switching to master branch'
git checkout master
check_status $?

echo "Updating jlv eclipse plugin version to $2-SNAPSHOT"
cd jlv-eclipse-plugin && mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=$2-SNAPSHOT
check_status $?

echo 'Commiting and pushing changes'
cd ..
git add .
git commit -m "jlv eclipse plugin version was updated to $2-SNAPSHOT"
git push
status=$?

if [ "$status" -gt 0 ]
then
    echo '[BUILD FAILED]'
else
    echo '[BUILD SUCCESS]'
fi

exit $status

