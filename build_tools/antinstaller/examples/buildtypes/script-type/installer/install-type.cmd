@echo off
REM #!/bin/sh

if  "%JAVA_HOME%" == "" goto nojava

if "%2" == "-type" set TYPE=-type %3
if "%1" == "-type" set TYPE=-type %2

set INTERFACE=default
set COMMAND=%JAVA_HOME%\bin\java
if "%1" == "text" goto settext
if "%1" == "swing" goto setswing
goto install

:settext
set COMMAND=%JAVA_HOME%\bin\java
set INTERFACE=text
goto install

:setswing
set COMMAND=start %JAVA_HOME%\bin\javaw
set INTERFACE=swing
goto install

:install


REM # Installer from command line classpath
set CLASSPATH=..\..\..\..\lib\xercesImpl.jar
set CLASSPATH=%CLASSPATH%;..\..\..\..\lib\xml-apis.jar
set CLASSPATH=%CLASSPATH%;..\..\..\..\lib\ant-installer.jar

REM # JGoodies Look And Feel
set CLASSPATH=%CLASSPATH%;..\..\..\..\lib\jgoodies-edited-1_2_2.jar
REM # Image resources
set CLASSPATH=%CLASSPATH%;..\..\..\..\classes

REM # minimal ANT classpath requirements
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant.jar
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant-launcher.jar
set CLASSPATH=%CLASSPATH%;cp

REM ### add other ant jars required here
REM #CLASSPATH=%CLASSPATH%;.\antlib\ant-weblogic.jar;.\antlib\ant-javamail.jar

%COMMAND% -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% . %TYPE%

goto end

:nojava
echo you must install java to run this applicaiton
goto end


:end
