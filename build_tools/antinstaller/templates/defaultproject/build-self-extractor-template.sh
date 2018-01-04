#!/bin/sh
dir=`expr  match "$0" '\(.*\)build-self-extractor.sh'`
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

# Ant classpath
ANT_INSTALLER_HOME=@ANT_INSTALL_ROOT@
CLASSPATH=$ANT_INSTALLER_HOME/antlib/ant.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALLER_HOME/antlib/xml-apis.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALLER_HOME/antlib/xercesImpl.jar
CLASSPATH=$CLASSPATH:$ANT_INSTALLER_HOME/antlib/ant-launcher.jar

$JAVA_HOME/bin/java -cp $CLASSPATH org.apache.tools.ant.Main -buildfile ./create-installer.xml 
