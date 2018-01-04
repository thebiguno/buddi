#!/bin/sh

# This istall script runs the installer in the development directory
# To create you own installers it is best to copy the structure
# and scripts in the ./demo directoy

# The default case runs the installer in an X session if it can
# falling back to text if the Graphics environment is not available
# This can be changed by uncommenting the if statement and the default
# can be changed by replacing "text" with "swing" below
# or passing the required option on the command line
GUI=$1
if [ "$GUI" = "" ] ; then
	GUI=default;
fi

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
CLASSPATH=$CLASSPATH:../../../../src
# Metouia (default) Look And Feel
# CLASSPATH=$CLASSPATH:./lib/metouia.jar

# minimal ANT classpath requirements
CLASSPATH=$CLASSPATH:../../../../antlib/ant.jar
CLASSPATH=$CLASSPATH:../../../../antlib/ant-launcher.jar
CLASSPATH=$CLASSPATH:cp

### add other ant jars required here
#CLASSPATH=$CLASSPATH:./antlib/ant-weblogic.jar:./antlib/ant-javamail.jar

COMMAND=$JAVA_HOME/bin/java

#echo $COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $1

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $GUI .
