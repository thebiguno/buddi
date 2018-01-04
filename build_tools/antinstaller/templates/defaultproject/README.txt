---+ Default Project Template Builder

This template is a method of getting up and running with
AntInstaller quickly.  The template will help you create
an installer for a Java project.  


---++ Output

   * create-installer.xml - An ant build script to package the installer
   * build.xml - An Ant script used by the installer
   * antinstall-config.xml - The definition of the installer
All of the above can be edited to suit your needs
   * Optionally the script will run the create-installer.xml Ant script to make the Jar immediately


---++ Usage

Read this README first
Change directory to the location of this README file
Set the ANT_INSTALL_HOME variable to the directory where you have installed AntInstaller.
Set JAVA_HOME
> export ANT_INSTALL_HOME=/usr/local/AntInstaller-beta0.7.2
Start the runtemplate.sh script
> ./runtemplate.sh
Follow the instructions

---++ Project Structure

AntInstaller launches and asks you for your project information
Generally it is assumed that your projects follow this structure

/projects_root/

/projects_root/project-name/

for example if you use eclipse and CVS this will be
/workspace/cvs-module-name/

By default the template selects the following subdirectories
which can be modified.
/projects_root/project-name/src/ - source code
/projects_root/project-name/bin/ - scripts (these get chmod u+x) in the installer
/projects_root/project-name/doc/ - documentation
/projects_root/project-name/lib/ - Jars
/projects_root/project-name/classes/ - err... classes?

The template only lets you select one directory for each, dont worry
AntInstaller itself is infinately configurable.


First time you run the script you should let the template
create a new empty directory for creating installers
/usr/local/installers/
From this directory subdirectories will be created for each project 
/usr/local/installers/project-name/

---++ Configuring the installer

This template is NOT designed to create you the final installer you will
deploy.  If you have a very simple standard project it may suffice, 
but it is expected that you will want to modify the XML files created
to customise the build.

Following the documentation on the web you can modify the 
antinstall-config.xml file to add extra pages and collect extra input 
when the user installs your application.
http://antinstaller.sourceforge.net

The build.xml file can be modified to customise the install proces
that is run by AntInstaller on the users machine.  
You have the full power of Ant so you can easily 
   * move files around
   * replace text
   * Zip Unzip
   * run scripts
   * call Java
   * run sub ant build.xml files
   * call SQL files via JDBC
   * fix line ending s
   * and anything in the CoreTypes in Ant.

The create-installer.xml file is an Ant script that creates the installer
by modifying this file you can add extra resources to the final Jar 
such as images that you want in the installer or extra source directories.
By adding Ant optional Jars to the installer you can then access any
additional features of Ant during the install enabling you to
   * Call SSH or Telnet commands
   * SCP/ FTP files 
   * Send emails
   * and anything in the OptionaTypes in Ant



---++ Contributions

This tool itself (obviously) uses AntInstaller.
You are free to modify the scripts to extend the features of the tool.
If you do I would encourage you to submit the changes back so
everyone can benefit from your enhancement.
It is a good example of using AntInstaller to acheive tasks other
than just installing apps.













