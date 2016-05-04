#!/bin/bash

SCRIPT_PATH=`readlink -f "$0"`;
cd `dirname ${SCRIPT_PATH}`
cd ..

PID_FILE=logs/jetty.pid

if [ ! -f $PID_FILE ]; then
  echo "server has not started!"
  exit 2
fi

PID=`cat $PID_FILE`
kill $PID
EXT_CODE=$?
if [ "$?" = "0" ]; then
    rm -f $PID_FILE
fi
exit $EXT_CODE
