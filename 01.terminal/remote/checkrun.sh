#!/bin/sh
script_abs=$(readlink -f "$0")
path=${script_abs%/bin*}


while true; do
	pid=$(ps -ef | grep ${project.name}-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}')
	if [[ "$pid" == "" ]]; then
	    echo `date +"%Y-%m-%d %H:%M:%S"` "未在运行,开始启动"
		cd ${path} && sh bin/start.sh $@
		sleep 120
	fi
	sleep 60
done
