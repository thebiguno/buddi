@echo off

REM # This script checks all the congurations

set DIR=%~dp0
cd %DIR%

echo using JAVA_HOME:%JAVA_HOME%

cd ifProperty\installer
echo ':::::::' checking ifProperty
call .\install.cmd %1
if not errorlevel = 0 echo "ifProperty failed"
        

cd ..\..\manytargets\artifacts
echo ':::::::' checking manytargets
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "manytargets failed"
        

cd ..\..\nonextract\artifacts
echo ':::::::' checking nonextract
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "nonextract failed"
        

cd ..\..\nonextract-auto\artifacts
echo ':::::::' checking nonextract-auto
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "nonextract-auto failed"
        

cd ..\..\nonextract-jdk1.5\artifacts
echo ':::::::' checking nonextract-jdk1.5
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "nonextract-jdk1.5 failed"
        

cd ..\..\nonextract-type\artifacts
echo ':::::::' checking nonextract-type
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "nonextract-type failed"
        

cd ..\..\releasenotes\artifacts
echo ':::::::' checking releasenotes
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "releasenotes failed"
        

cd ..\..\script\installer
echo ':::::::' checking script
call .\install.cmd %1
if not errorlevel = 0 echo "script failed"
        

cd ..\..\script-auto\installer
echo ':::::::' checking script-auto
call .\install.cmd %1
if not errorlevel = 0 echo "script-auto failed"
        

cd ..\..\script-graphical-pp\installer
echo ':::::::' checking script-graphical-pp
call .\install.cmd %1
if not errorlevel = 0 echo "script-graphical-pp failed"
        

cd ..\..\script-laf\installer
echo ':::::::' checking script-laf
call .\install.cmd %1
if not errorlevel = 0 echo "script-laf failed"
        

cd ..\..\script-lang\installer
echo ':::::::' checking script-lang
call .\install.cmd %1
if not errorlevel = 0 echo "script-lang failed"
        

cd ..\..\script-no-xerces\installer
echo ':::::::' checking script-no-xerces
call .\install.cmd %1
if not errorlevel = 0 echo "script-no-xerces failed"
        

cd ..\..\script-overflow\installer
echo ':::::::' checking script-overflow
call .\install.cmd %1
if not errorlevel = 0 echo "script-overflow failed"
        

cd ..\..\script-regex\installer
echo ':::::::' checking script-regex
call .\install.cmd %1
if not errorlevel = 0 echo "script-regex failed"
        

cd ..\..\script-type\installer
echo ':::::::' checking script-type
call .\install-type.cmd %1
if not errorlevel = 0 echo "script-type failed"
call .\install-type.cmd %1 -type alternative
if not errorlevel = 0 echo "script-type failed"
        

cd ..\..\script-wide\installer
echo ':::::::' checking script-wide
call .\install.cmd %1
if not errorlevel = 0 echo "script-wide failed"
        

cd ..\..\selfextract\artifacts
echo ':::::::' checking selfextract
%JAVA_HOME%\bin\java -jar *.jar %1
if not errorlevel = 0 echo "selfextract failed"
        

cd ..\..

