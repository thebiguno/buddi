#!/bin/sh

dir=`expr  match "$0" '\(.*\)runtemplate.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
      echo changing to $dir
      cd "$dir" ;
   fi
fi

if [ "$ANT_INSTALL_HOME" == "" ] ; then 
  export ANT_INSTALL_HOME=../..
fi

if [ ! -f $ANT_INSTALL_HOME/lib/ant-installer.jar ] ; then 
  echo "set ANT_INSTALL_HOME"
  exit 1;
fi

if [ "$JAVA_HOME" = "" ] ; then
	echo set JAVA_HOME;
	exit 1;
fi


CLASSPATH=$ANT_INSTALL_HOME/lib/xercesImpl.jar:$ANT_INSTALL_HOME/lib/xml-apis.jar:$ANT_INSTALL_HOME/lib/ant-installer.jar:$ANT_INSTALL_HOME/lib/ai-icons-eclipse.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALL_HOME/antlib/ant.jar:$ANT_INSTALL_HOME/antlib/ant-launcher.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALL_HOME/lib/jgoodies-edited-1_2_2.jar
CLASSPATH=$CLASSPATH:./cp
CLASSPATH=$CLASSPATH:../../images/navicons/amaranth

COMMAND=$JAVA_HOME/bin/java
INTERFACE=swing
if [ "$1" = "text" ] ; then
	INTERFACE=text;
fi

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $INTERFACE .
