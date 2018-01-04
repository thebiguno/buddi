@echo off

if  "%JAVA_HOME%" == "" goto nojava

set DIR=%~dp0
cd %DIR%

cd .\templates\defaultproject
call runtemplate.cmd %1
cd ..\..
goto end

:nojava
echo set JAVA_HOME
echo you must install java use the wizard
pause
goto end

:end