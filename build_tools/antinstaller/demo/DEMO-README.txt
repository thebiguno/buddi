This Demo is a very simple example of what AntInstaller and Ant can do

The installer performs the following tasks
extracts an installpack.zip pack of files to a temporary directory
install the selected components in the directory of the users choice
performs an ant replace operation on the config file in {installDir}/config/example-config.xml
deletes the temporary direcotry

Clearly this is not a realworld example. 

The replace Ant task is a very usefull operation for installations, and of course the
full power of ant is at your disposal at the time the installation is performed.  It is probably
best to be specific about the optional tasks that are included.
