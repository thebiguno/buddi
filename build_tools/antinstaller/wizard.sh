#!/bin/sh

dir=`expr  match "$0" '\(.*\)wizard.sh'`
#  if it we did not call ./  change to the directory we called
if [ "$dir" != "./" ] ; then
   if [ "$dir" != "" ] ;  then
      echo changing to $dir
      cd "$dir" ;
   fi
fi
./templates/defaultproject/runtemplate.sh $1
