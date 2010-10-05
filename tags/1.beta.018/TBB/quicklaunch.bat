:: -------------------------------------------------------------------
::
:: This is a MS-DOS script, it works only on Windows operating systems.
:: It launches TBB with the appropriate parameters.
::
:: v.0.2
::
:: -------------------------------------------------------------------
::
:: Total Boum Boum
:: Copyright 2008-2010 Vincent Labatut 
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
	Set japa=.\resources\lib\javaparser-1.0.7.jar
	Set cp=%bin%;%jdom%;%japa%;%ai%
	Set launcher=org.totalboumboum.Launcher

:: launch the game
	java -Xmx256m -classpath %cp% %launcher% quick

::	pause

	Endlocal
