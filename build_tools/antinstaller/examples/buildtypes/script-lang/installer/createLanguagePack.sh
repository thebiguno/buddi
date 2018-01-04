#!/bin/sh

# This script creates properties files stubs from the antinstall-config.xml file
#
# it only works if attributes are placed on separate lines

dir=`expr  match "$0" '\(.*\)createResourceStub.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
      cd "$dir" ;
   fi
fi

if [ "$ANT_INSTALL_HOME" == "" ] ; then 
  export ANT_INSTALL_HOME=../../../..
fi

if [ ! -f $ANT_INSTALL_HOME/lib/ant-installer.jar ] ; then 
  echo "set ANT_INSTALL_HOME"
  exit 1;
fi

if [ "$JAVA_HOME" = "" ] ; then
	echo set JAVA_HOME;
	exit 1;
fi


CLASSPATH=$ANT_INSTALL_HOME/lib/xercesImpl.jar:$ANT_INSTALL_HOME/lib/xml-apis.jar:$ANT_INSTALL_HOME/lib/ant-installer.jar:$ANT_INSTALL_HOME/lib/ant-installer-ext.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALL_HOME/antlib/ant.jar:$ANT_INSTALL_HOME/antlib/ant-launcher.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALL_HOME/lib/jgoodies-edited-1_2_2.jar
CLASSPATH=$CLASSPATH:./cp

COMMAND=$JAVA_HOME/bin/java

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.util.CreateLanguagePack
