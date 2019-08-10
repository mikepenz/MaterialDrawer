#!/bin/bash

./gradlew clean build

if [ "$1" = "release" ];
then
    ./gradlew library:bintrayUpload -Plibrary_only
    ./gradlew library-nav:bintrayUpload -x test -x lint -Plibrary_nav_only
else
    //TODO
fi
