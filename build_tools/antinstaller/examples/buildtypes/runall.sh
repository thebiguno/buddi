#!/bin/sh
########################################
#
# This script checks all the configurations
#
########################################

# Get path
if (echo "$0" | grep '/' >/dev/null)
then
	  # Called-by Prog name includes path
	  dir=`dirname $0`   # Get filedir from call
else
	  dir=`pwd`
fi

# Ensure absolute path
if (echo "$dir" | grep '^/' >/dev/null)
then
	  # Already an absolute path
	  :
else
	  dir=`pwd`/$dir
fi

echo using JAVA_HOME:$JAVA_HOME

for i in `echo *`
do
	cd ${dir}
	EXAMPLE_INSTALLER_DIR=${dir}/${i}
	if [ ! -d ${EXAMPLE_INSTALLER_DIR} ]
	then
		continue;
	fi

	cd ${EXAMPLE_INSTALLER_DIR} || { echo "Unable to change directory to ${EXAMPLE_INSTALLER_DIR}"; exit 10; }

	echo "::::::: checking ${i}"

	if [ -f installer/install.sh ]
	then
		cd installer || { echo "Unable to change directory to ${EXAMPLE_INSTALLER_DIR}/artifacts/installer"; exit 10; }
	
		./install.sh
		STATUS=$?
		if [ $STATUS != 0 ]
		then
			echo "${i} failed"
			exit $STATUS
		fi

		if [ -f antinstall-config-alternative.xml ]
		then
			./install.sh -type alternative
			STATUS=$?

			if [ $STATUS != 0 ]
			then
				echo "${i} failed"
				exit $STATUS
			fi
		fi
	else
		if [ -d artifacts ]
		then
			cd artifacts || { echo "Unable to change directory to ${EXAMPLE_INSTALLER_DIR}/artifacts"; exit 10; }
			$JAVA_HOME/bin/java -jar *.jar
			STATUS=$?
			if [ $STATUS != 0 ]
			then
				echo "${i} failed"
				exit $STATUS
			fi

			if [ -f ../installer/antinstall-config-alternative.xml ]
			then
				$JAVA_HOME/bin/java -jar *.jar -type alternative
				STATUS=$?
				if [ $STATUS != 0 ]
				then
					echo "${i} failed"
					exit $STATUS
				fi
			fi
		else
			echo "Skipping ${i} as don't know how to run it"
		fi
	fi
done
exit $STATUS
