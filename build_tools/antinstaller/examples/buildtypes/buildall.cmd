@echo off

REM # This script build all the installers


echo using JAVA_HOME:%JAVA_HOME%

REM multiple Ant versions cause errors so unset the classpath
set CLASSPATH=

cd manytargets\build
echo ':::::::' building manytargets
call ant

cd ..\..\nonextract\build
echo ':::::::' building nonextract
call ant

cd ..\..\nonextract-auto\build
echo ':::::::' building nonextract-auto
call ant

cd ..\..\nonextract-jdk1.5\build
echo ':::::::' building nonextract-jdk1.5
call ant

cd ..\..\nonextract-type\build
echo ':::::::' building nonextract-type
call ant

cd ..\..\releasenotes\build
echo ':::::::' building releasenotes
call ant

cd ..\..\selfextract\build
echo ':::::::' building selfextract
call ant

cd ..\..


