#!/bin/sh

script_abs=$(readlink -f "$0")
path=${script_abs%/bin*}

echo $path
java -jar $path/${project.name}-1.0-SNAPSHOT.jar 1>$path/log.log 2>$path/log.log &
