#!/bin/bash
# -------------------------------------------------------------------
#
# This is a BASH (Bourne-Again SHell) script, 
# it should work on most Linux, Unix, and Mac OS systems.
# It (re)compiles the whole TBB game and the dependant AI classes.
#
# v.0.6
#
# -------------------------------------------------------------------
#
# Total Boum Boum
# Copyright 2008-2012 Vincent Labatut 
# 
# This file is part of Total Boum Boum.
# 
# Total Boum Boum is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 2 of the License, or
# (at your option) any later version.
# 
# Total Boum Boum is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
# 
# -------------------------------------------------------------------
#
# change current directory
	current=$(readlink -f "$0")
	path=`dirname "$current"`
	cd ${path}
#
# define path variables
	main="./src/org/totalboumboum"
	aig=${main}/ai
	aib="./resources/ai"
	ai=${aib}/org/totalboumboum/ai
	bin="./bin"
	jdom="./resources/lib/jdom.jar"
	japa="./resources/lib/javaparser-1.0.8.jar"
	gral="./resources/lib/gral-core-0.9-SNAPSHOT.jar"
	cp="${bin}:${jdom}:${japa}:${gral}"
	sp="./src"
#
# create directory for .class files
	if [ -d $bin ]; then
		echo "${bin} already exists"
	else
		mkdir $bin
	fi	
#
# (re)compile the game
	echo "compiling the game..."
	javac -nowarn -sourcepath $sp -classpath $cp ${main}/Launcher.java -d $bin
# (re)compile the AI classes located in the game
	echo "compiling the AI classes inside the game..."
	for i in $(ls -d ${aig}/*/); do 
		echo -e "\t${i}"
		for j in $(find $i -name "*.java" -print -type f); do 
			echo -e "\t\t${j}"
			javac -nowarn -sourcepath $sp -classpath $cp $j -d $bin
		done
	done
#
# (re)compile the AI classes located in resources/ai
	echo "compiling the AI classes located in the resources..."
	for i in $(ls -d ${ai}/*/); do
		echo -e "\t${i}"
		for j in $(ls -d ${i}/ais/*/); do
			echo -e "\t\t${j}"	
			javac -nowarn -sourcepath ${sp}:${aib} -classpath ${cp}:${aib} $j/*.java
		done
	done
#
#

