
This SelfExtracting archive should be run with Java to start the installation.
________________________________________________________________________________

If Java1.4 is installed correctly simply double click the .jar file in your 
file browser.


To run the installer from the commandline
WINDOWS 
		C:\> javaw -jar [installer-file].jar
UNIX
		> java -jar [installer-file].jar


If the .jar extension has been reregistered you will need to specify the Java
command

WINDOWS

	Right mouse click on the file and select "Open with" then "javaw"


LINUX

	Konquerer (KDE) right mouse click and select "Open with" then "java"





TROUBLESHOOTING
________________________________________________________________________________


WINDOWS
________________________________________________________________________________
If you do not have the "javaw" entry in the "Open with" list 
	Check that you have a current version of Java ( >1.4 ) installed.
	The Sun Java installation process registers javaw with .jars in the 
	installation.  
	You can reinstall Java if you still have the original installer.
	If you don't have the installer, or don't want to reinstall Java, you can 
	modify the registration of the .jar extension.
		In Explorer select "Tools" then "Folder options"
		Select the "File types" tab
		First check to see if teh JAR Extension is in the list
			JAR extension IS in the list
				click the "Change" button
				find javaw in the list of files
				click OK
			JAR extension NOT in list
				click the New button
				enter "jar" as the file extension
				Enter <New> as the file type
				
			
				


LINUX
________________________________________________________________________________

Using X

Log in to the shell as the user that is running X
Ensure you have exported your DISPLAY variable  (echo $DISPLAY) if not export it
> export DISPLAY=127.0.0.1:0

Check the default java is the correct version
> java -version

	If the java binary is not found, but you do have Java installed
		Add the bin directory of the Java installation to your path
		> export PATH=$PATH:[path to Java]/bin
		run the installer with > java -jar [installer-file].jar
	
	If the default java version is below 1.4
		If you do NOT have a recent version installed anywhere on your system
			install the Sun Java JRE or SDK for linux.
		If/when you DO have a recent version of Java installed
			find the location of the java installation
			e.g /usr/java/j2sdk1.4.2_04
			export the JAVA_HOME variable of the installation
			> export JAVA_HOME=/usr/java/j2sdk1.4.2_04
			run the following command
			> $JAVA_HOME/bin/java -jar [installer-file].jar
			You should consider adding the export JAVA_HOME entry to your profile 
			and/or ensure the location of the JAVA_HOME/bin directory for Java >1.4 
			is in your PATH
		
	If the default java version IS above 1.4
	run the installer with > java -jar [installer-file].jar


Without X (command line)

The installer will run on the command line with a text interface if it is not 
possible to load the GUI.
running the following command without a DISPLAY variable or in runlevel 3 will
start the installer in text mode
> java -jar [installer-file].jar






 