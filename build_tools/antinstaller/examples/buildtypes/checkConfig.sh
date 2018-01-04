#!/bin/sh

# This script checks all the configurations

cd "$(dirname "$0")"

echo
echo
echo STARTING TESTS
echo please wait...
echo
echo

CD=`pwd`

# Iterate over the directories
for dir in `echo *`
do
	if [ -f $CD/$dir/checkConfig.sh ] ; then
		echo ':::::::' checking $dir 1>> $CD/output
		$CD/$dir/checkConfig.sh 1>> $CD/output
		if [ $? != 0 ] ; then
			echo "$dir failed"
			exit $? ;
		fi
	fi
done

cd $CD
cat output
echo
echo
echo
WARN=`cat output | grep Warning | wc -l`
if [ "$WARN" -ne "0" ] ; then
	echo Warnings: $WARN, see above for details
	cat output | grep Warning
fi

ERR=`cat output | grep Error | wc -l`
if [ "$ERR" -ne "0" ] ; then
        echo Error: $ERR, see above for details
        cat output | grep Error
fi


rm output
