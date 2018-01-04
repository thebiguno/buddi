@echo off
REM #!/bin/sh

REM # This install script runs the installer in the development directory
REM # To create you own installers it is best to copy the structure
REM # and scripts in the ./demo directoy

REM # The default case runs the installer in an X session if it can
REM # falling back to text if the Graphics environment is not available
REM # This can be changed by uncommenting the if statement and the default
REM # can be changed by replacing "text" with "swing" below
REM # or passing the required option on the command line

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
set CLASSPATH=%CLASSPATH%;..\..\

REM # minimal ANT classpath requirements
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant.jar
set CLASSPATH=%CLASSPATH%;..\..\..\..\antlib\ant-launcher.jar

REM ### add other ant jars required here
REM #CLASSPATH=$CLASSPATH;.\antlib\ant-weblogic.jar;.\antlib\ant-javamail.jar

%COMMAND% -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% .

goto end

:nojava
echo you must install java to run this applicaiton
goto end


:end