#!/bin/bash

if [ "$*" == "" ]
then
	echo 
	echo "Village Elder supports the following commands:"
	echo "   version		Prints out version information."
	echo "   index		Creates an index from a repository."

	echo 
	echo "Use -? with any of these commands for additional help."
	echo 
else
	java -cp .:VillageElder.jar com.fuerve.villageelder.client.commandline.Main $*
fi


