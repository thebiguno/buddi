#!/bin/sh

# This script checks the configuration



dir=`expr  match "$0" '\(.*\)checkConfig.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
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
cd installer
#CLASSPATH=../../../../lib/xercesImpl.jar
#CLASSPATH=$CLASSPATH:../../../../lib/xml-apis.jar
CLASSPATH=../../../../lib/ant-installer.jar
CLASSPATH=$CLASSPATH:../../../../antlib/ant.jar

COMMAND=$JAVA_HOME/bin/java

#echo $COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $1

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ConfigurationLoader .
if [ $? != 0 ] ; then
        exit $? ;
fi

