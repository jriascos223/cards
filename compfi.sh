#!/bin/bash

dir=$(pwd -P)
cd lib
echo "Removing old javafx dependency..."
rm -rf "javafx-sdk-11.0.2"
if [ $OSTYPE == "msys" ]
then
    unzip -nq openjfx-11.0.2_windows-x64_bin-sdk
else
    unzip -nq javafx-sdk-11.0.2.zip
    rm -rf __MACOSX
fi
echo "Unzipped JavaFX."
jfxpath='lib/javafx-sdk-11.0.2/lib'
cd ..
javac -d bin --module-path $jfxpath --add-modules javafx.controls src/tech/jriascos/*/*.java
echo "Compiled project."
echo "Running main function found in tech.jriascos.application.Window."
java -classpath $dir/bin:$dir/lib/ --module-path $jfxpath --add-modules javafx.controls tech.jriascos.application.Window
