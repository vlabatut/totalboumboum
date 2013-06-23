:: -------------------------------------------------------------------
::
:: This is a MS-DOS script, it works only on Windows operating systems.
:: It launches TBB with the appropriate parameters.
:: You can change the value of the Xmx parameter in order
:: to increase the amount of memory used by the Java machine. 
::
:: v.0.3
::
:: -------------------------------------------------------------------
::
:: Total Boum Boum
:: Copyright 2008-2013 Vincent Labatut 
:: 
:: This file is part of Total Boum Boum.
:: 
:: Total Boum Boum is free software: you can redistribute it and/or modify
:: it under the terms of the GNU General Public License as published by
:: the Free Software Foundation, either version 2 of the License, or
:: (at your option) any later version.
:: 
:: Total Boum Boum is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
:: GNU General Public License for more details.
:: 
:: You should have received a copy of the GNU General Public License
:: along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
:: 
:: -------------------------------------------------------------------

	Setlocal

:: define path variables
	Set ai=.\resources\ai
	Set bin=.\bin
	Set jdom=.\resources\lib\jdom.jar
	Set japa=.\resources\lib\javaparser-1.0.8.jar
	Set gral=.\resources\lib\gral-core-0.9-SNAPSHOT.jar
	Set cp=%bin%;%jdom%;%japa%;%gral%;%ai%
	Set launcher=org.totalboumboum.Launcher

:: launch the game
	java -Xmx256m -classpath %cp% %launcher% quick

::	pause

	Endlocal
