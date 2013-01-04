:: -------------------------------------------------------------------
::
:: This is an MS-DOS script, it works only on Windows operating systems.
:: It (re)compiles the whole TBB game (and all AIs located in resources/ai).
:: v.0.5
::
:: -------------------------------------------------------------------
::
:: Total Boum Boum
:: Copyright 2008-2012 Vincent Labatut 
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

	@echo off
	Setlocal

:: define path variables
	Set main=.\src\org\totalboumboum
	Set aig=%main%\ai
	Set aib=.\resources\ai
	Set ai=%aib%\org\totalboumboum\ai
	Set bin=.\bin
	Set jdom=.\resources\lib\jdom.jar
	Set japa=.\resources\lib\javaparser-1.0.8.jar
	Set gral=.\resources\lib\gral-core-0.9-SNAPSHOT.jar
	Set cp=%bin%;%jdom%;%japa%;%gral%
	Set sp=.\src

:: create directory for .class files
	If exist %bin% goto EXISTS
	mkdir %bin%
	goto COMPILE
:EXISTS
	echo %bin% already exists

:: (re)compile the game
:COMPILE	
	echo compiling the game...
	javac -nowarn -sourcepath %sp% -classpath %cp% %main%\Launcher.java -d %bin%

:: (re)compile the AI classes inside the game
	echo compiling the AI classes inside the game...
	For /d %%f In (%aig%\*) Do (
		echo 	%%f	
		dir %%f\*.java /b /s /x > sources.txt
		For /f "delims=;" %%g In (sources.txt) Do (
			echo 		%%g
			javac -nowarn -sourcepath %sp% -classpath %cp% "%%g" -d %bin%
		)
	)
	del sources.txt

:: (re)compile the AI classes located in resources\ai
	echo compiling the AI classes located in the resources... 
	For /d %%f In (%ai%\*) Do (
		echo 	%%f
		For /d %%g In (%%f\ais\*) Do ( 
			echo 	 	%%g
			javac -nowarn -sourcepath %sp%;%aib% -classpath %cp%;%aib% %%g\*.java
		)
	)

	pause

	Endlocal
