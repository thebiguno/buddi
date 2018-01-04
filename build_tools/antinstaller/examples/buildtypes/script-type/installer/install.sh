#!/bin/sh

# This script is NOT the same as the other install.sh scripts since
# it handles the command line parameters differently

# This istall script runs the installer in the development directory
# To create you own installers it is best to copy the structure
# and scripts in the ./demo directoy

dir=`expr  match "$0" '\(.*\)install.sh'`
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
CLASSPATH=../../../../lib/xercesImpl.jar
CLASSPATH=$CLASSPATH:../../../../lib/xml-apis.jar
CLASSPATH=$CLASSPATH:../../../../lib/ant-installer.jar

# JGoodies Look And Feel
CLASSPATH=$CLASSPATH:../../../../lib/jgoodies-edited-1_2_2.jar
# Image resources
CLASSPATH=$CLASSPATH:../../

# minimal ANT classpath requirements
CLASSPATH=$CLASSPATH:../../../../antlib/ant.jar
CLASSPATH=$CLASSPATH:../../../../antlib/ant-launcher.jar
CLASSPATH=$CLASSPATH:cp

### add other ant jars required here
#CLASSPATH=$CLASSPATH:./antlib/ant-weblogic.jar:./antlib/ant-javamail.jar

COMMAND=$JAVA_HOME/bin/java

GUI=swing
if [ "$1" == "text" ] ; then
	GUI=text;
	shift;
fi

if [ "$1" != "" ] ; then
	if [ "$1" != "-type" ] ; then
		echo 'Usage ./install-type.sh [text] [-type alternative]'
	fi
fi
$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $GUI . $*
