#!/bin/sh
####################################################
#
# This script builds all the installers
# Usage: buildall.sh [other_args_to_ant_like_-v]
#
####################################################

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

# Iterate over the directories
for i in `echo *`
do
	cd ${dir}
	INSTALLER_BUILD_DIR=${dir}/${i}/build
	if [ ! -d ${INSTALLER_BUILD_DIR} ]
	then
		continue;
	fi

	cd ${INSTALLER_BUILD_DIR} || { echo "Unable to change directory to ${INSTALLER_BUILD_DIR}"; exit 10; }

	if [ ! -f build.xml ]
	then
		echo "Skipping ${i} as no build.xml file"
		continue
	fi

	echo "::::::: building ${i}"
	ant $*
	STATUS=$?
	if [ $STATUS != 0 ]
	then
		echo "build of ${i} failed"
		exit $STATUS
	fi
done

exit $STATUS