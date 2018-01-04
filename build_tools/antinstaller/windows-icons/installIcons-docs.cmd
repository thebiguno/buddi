@echo off
REM this batch file exists so that the parameters of this line are NOT quoted
rundll32 setupapi,InstallHinfSection DefaultInstall 128 @installDir@\windows-icons\installIcons-docs.inf
