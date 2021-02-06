#!/bin/bash

if [[ "$1" = "clean" ]];
then
    ./gradlew clean build
fi

if [[ "$1" = "release" || "$2" = "release" ]];
then
    ./gradlew materialdrawer:publishReleasePublicationToSonatypeRepository -Plibrary_only
    ./gradlew materialdrawer-nav:publishReleasePublicationToSonatypeRepository -x test -x lint -Plibrary_nav_only
    ./gradlew materialdrawer-iconics:publishReleasePublicationToSonatypeRepository -x test -x lint -Plibrary_iconics_only
else
    //TODO
fi
