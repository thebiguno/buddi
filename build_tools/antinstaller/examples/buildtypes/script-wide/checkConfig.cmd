@echo off

REM  This script check the antinstall-config.xml configuration in the current directory


REM Installer requires Java
if  "%JAVA_HOME%" == "" goto setjava

set CLASSPATH=..\..\..\lib\xercesImpl.jar
set CLASSPATH=%CLASSPATH%;..\..\..\lib\xml-apis.jar
set CLASSPATH=%CLASSPATH%;..\..\..\lib\ant-installer.jar
set CLASSPATH=%CLASSPATH%;..\..\..\antlib\ant.jar


%JAVA_HOME%\bin\java -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ConfigurationLoader installer
goto end

:setjava
echo Installer requires Java available from http://java.sun.com
echo If you have Java installed you may just need to set the variable JAVA_HOME

:end
pause