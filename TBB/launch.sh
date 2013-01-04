#!/bin/bash
# -------------------------------------------------------------------
#
# This is a BASH (Bourne-Again SHell) script, 
# it should work on most Linux, Unix, and Mac OS systems.
# It launches TBB with the appropriate parameters.
# You can change the value of the Xmx parameter in order
# to increase the amount of memory used by the Java machine. 
#
# v.0.4
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
	ai="./resources/ai"
	bin="./bin"
	jdom="./resources/lib/jdom.jar"
	japa="./resources/lib/javaparser-1.0.8.jar"
	gral="./resources/lib/gral-core-0.9-SNAPSHOT.jar"
	cp="${bin}:${jdom}:${japa}:${gral}:${ai}"
	launcher="org.totalboumboum.Launcher"
	splash="./resources/gui/images/splash.png"
#
# launch the game
	java -Xmx256m -splash:$splash -XX:-UseConcMarkSweepGC -classpath $cp $launcher
#
#
