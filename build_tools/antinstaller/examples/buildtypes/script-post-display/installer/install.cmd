@echo off
REM #!/bin/sh

if  "%JAVA_HOME%" == "" goto nojava

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
set CLASSPATH=%CLASSPATH%;..\..\..\..\lib\ai-icons-eclipse.jar

REM # JGoodies Look And Feel
set CLASSPATH=%CLASSPATH%;..\..\..\..\lib\jgoodies-edited-1_2_2.jar
REM # Image resources
set CLASSPATH=%CLASSPATH%;..\..\..\..\classes
set CLASSPATH=%CLASSPATH%;cp

REM # minimal ANT classpath requirements
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant.jar
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant-launcher.jar

%COMMAND% -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% .

goto end

:nojava
echo you must install java to run this applicaiton
goto end


:end