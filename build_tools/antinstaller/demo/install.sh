#!/bin/sh

# find the command called's root  e.g.  ./build/
dir=`expr  match "$0" '\(.*\)install.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
      echo changing to $dir
      cd "$dir" ;
   fi
fi



if [ "$JAVA_HOME" = "" ] ; then
	echo set JAVA_HOME;
	exit 1;
fi

AI_LIB=../lib
ANT_LIB=../antlib
CLASSPATH=${AI_LIB}/xercesImpl.jar:${AI_LIB}/xml-apis.jar:${AI_LIB}/ant-installer.jar
CLASSPATH=$CLASSPATH:${AI_LIB}/jgoodies-edited-1_2_2.jar
CLASSPATH=$CLASSPATH:${ANT_LIB}/ant.jar:${ANT_LIB}/ant-launcher.jar
CLASSPATH=$CLASSPATH:./installclasspath


### add other ant jars required here and add them to the install package
#CLASSPATH=$CLASSPATH:./installlib/ant-weblogic.jar:./installlib/ant-javamail.jar

COMMAND=$JAVA_HOME/bin/java
INTERFACE=default
if [ "$1" != "" ] ; then
	INTERFACE=$1;
fi

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $INTERFACE .
