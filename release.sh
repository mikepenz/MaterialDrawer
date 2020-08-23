#!/bin/bash

if [[ "$1" = "clean" ]];
then
    ./gradlew clean build
fi

if [[ "$1" = "release" || "$2" = "release" ]];
then
    ./gradlew library:bintrayUpload -Plibrary_only
    ./gradlew library-nav:bintrayUpload -x test -x lint -Plibrary_nav_only
    ./gradlew library-iconics:bintrayUpload -x test -x lint -Plibrary_iconics_only
else
    //TODO
fi
