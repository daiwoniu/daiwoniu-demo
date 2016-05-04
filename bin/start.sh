#!/bin/bash

SCRIPT_PATH=`readlink -f "$0"`;
cd `dirname ${SCRIPT_PATH}`
cd ..

mkdir -p logs
PID_FILE=logs/jetty.pid

PID=""
if [ -f $PID_FILE ]; then
  PID=`cat $PID_FILE`
fi

if [ ! "$PID" = "" ]; then
  if ( /bin/ps -p $PID >/dev/null 2>&1 ); then
    echo "already started, pid = $PID"
    exit 1
  fi
fi

CP="etc_test"

if [ -d target/classes ]; then
  CP=$CP:target/classes
fi
if [ -d webapp/WEB-INF/classes ]; then
  CP=$CP:webapp/WEB-INF/classes
fi
for jar in `/bin/ls -1 lib/*.jar`
do
  CP=$CP:$jar
done

WEBAPP_DIR="src/main/webapp"
if [ -d "webapp" ]; then
  WEBAPP_DIR="webapp"
fi

nohup java -Xms512m -Xmx512m -Dorg.eclipse.jetty.server.Request.maxFormContentSize=0 -cp $CP com.woniu.base.web.Main -p 8080 -w $WEBAPP_DIR $@ &
echo $! >$PID_FILE
