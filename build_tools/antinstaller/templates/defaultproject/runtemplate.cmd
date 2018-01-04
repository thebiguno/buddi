@echo off

if  "%JAVA_HOME%" == "" goto nojava
if  "%ANT_INSTALL_HOME%" == "" goto trydot

goto setclasspath

:trydot
if EXIST ..\..\lib\ant-installer.jar set ANT_INSTALL_HOME=..\..
if NOT EXIST ..\..\lib\ant-installer.jar goto noai
echo ANT_INSTALL_HOME = %ANT_INSTALL_HOME%

:setclasspath
REM Ant classpath
set CLASSPATH=%ANT_INSTALL_HOME%\antlib\ant.jar;%ANT_INSTALL_HOME%\antlib\ant-launcher.jar

REM Images classpath
set CLASSPATH=%CLASSPATH%;.\cp
set CLASSPATH=%CLASSPATH%;..\..\images\navicons\amaranth

REM XML classpath
set CLASSPATH=%CLASSPATH%;%ANT_INSTALL_HOME%\lib\xercesImpl.jar;%ANT_INSTALL_HOME%\lib\xml-apis.jar

REM AntInstaller classpath
set CLASSPATH=%CLASSPATH%;%ANT_INSTALL_HOME%\lib\ant-installer.jar
set CLASSPATH=%CLASSPATH%;%ANT_INSTALL_HOME%\lib\jgoodies-edited-1_2_2.jar

if "%1" == "text" goto settext
if "%1" == "swing" goto setswing
goto setswing

:settext
set COMMAND=%JAVA_HOME%\bin\java
set INTERFACE=text
goto install

:setswing
set COMMAND=%JAVA_HOME%\bin\javaw
set INTERFACE=swing
goto install

:install
start "Antinstaller" "%COMMAND%" -classpath "%CLASSPATH%"  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% .
goto end

:nojava
echo set JAVA_HOME
echo you must install java to install this application
goto end

:noai
echo set ANT_INSTALL_HOME
echo you must install AntInstaller to install this application
goto end

:end