#!/bin/sh

# This script check the configuration in the scripts directory

# find the command called's root  e.g.  ./build/
dir=`expr  match "$0" '\(.*\)checkConfig.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
      echo changing to $dir
      cd "$dir" ;
   fi
fi


# Installer requires Java
if [ "$JAVA_HOME" = "" ] ; then
	echo Installer requires Java available from http://java.sun.com
	echo If you have Java installed you may just need to set the variable JAVA_HOME
	exit 1;
fi

# Installer from command line classpath
CLASSPATH=./installlib/xercesImpl.jar
CLASSPATH=$CLASSPATH:./installlib/xml-apis.jar
CLASSPATH=$CLASSPATH:./installlib/ant-installer.jar
CLASSPATH=$CLASSPATH:./installlib/ant-installer-ext.jar
CLASSPATH=$CLASSPATH:./antlib/ant.jar
CLASSPATH=$CLASSPATH:./antlib/ant-launcher.jar

COMMAND=$JAVA_HOME/bin/java

#echo $COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $1

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ConfigurationLoader .
