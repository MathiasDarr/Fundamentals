#!/bin/bash
if [ -z "`echo $JAVA_VERSION | grep version`" ] ; then
  echo sf
else
  echo "NO"
fi