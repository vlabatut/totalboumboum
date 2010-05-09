:: -------------------------------------------------------------------
::
:: this is a MS-DOS script, it works only on Windows operating systems.
:: it launches TBB, but you can preferably use TBB.exe
::
:: v.0.1
::
:: -------------------------------------------------------------------
::
:: Total Boum Boum
:: Copyright 2008-2009 Vincent Labatut 
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
	Set cp=%bin%;%jdom%;%ai%
	Set launcher=fr.free.totalboumboum.Launcher
	Set splash=.\resources\gui\images\splash.png

:: launch the game
	java -Xmx256m -splash:%splash% -classpath %cp% %launcher%

:: 	pause

	Endlocal
