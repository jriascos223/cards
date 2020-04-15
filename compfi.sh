#!/bin/bash

dir=$(pwd -P)
jfxpath='lib/javafx-sdk-11.0.2/lib'
javac -d bin --module-path $jfxpath --add-modules javafx.controls src/tech/jriascos/*/*.java
echo "Compiled project."
echo "Running main function found in tech.jriascos.application.Window."
java -classpath $dir/bin:$dir/lib/ --module-path $jfxpath --add-modules javafx.controls tech.jriascos.application.Window
