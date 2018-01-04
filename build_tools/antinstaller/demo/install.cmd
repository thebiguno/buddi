@echo off
REM #!/bin/sh
if  "%JAVA_HOME%" == "" goto nojava

setlocal

set AI_LIB=..\lib
set ANT_LIB=..\antlib
set CLASSPATH=%AI_LIB%\xercesImpl.jar;%AI_LIB%\xml-apis.jar;%AI_LIB%\ant-installer.jar
set CLASSPATH=%CLASSPATH%;%AI_LIB%\jgoodies-edited-1_2_2.jar
set CLASSPATH=%CLASSPATH%;%ANT_LIB%\ant.jar;%ANT_LIB%\ant-launcher.jar
set CLASSPATH=%CLASSPATH%;.\installclasspath



REM set CLASSPATH=%CLASSPATH%;.\installlib\ant-weblogic.jar;.\installlib\ant-javamail.jar

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
set COMMAND=%JAVA_HOME%\bin\javaw
set INTERFACE=swing
goto install

:install

start "AntInstaller" "%COMMAND%" -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% .
goto end

:nojava
echo you must install java to run this applicaiton
goto end

endlocal
:end