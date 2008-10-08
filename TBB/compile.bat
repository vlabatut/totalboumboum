:: -------------------------------------------------------------------
::
:: this is an MS-DOS script, it works only on Windows operating systems.
:: it (re)compiles the whole TBB game
::
:: -------------------------------------------------------------------

	Setlocal

:: define path variables
	Set main=.\src\fr\free\totalboumboum
	Set aig=%main%\ai
	Set ai=.\resources\ai
	Set bin=.\bin
	Set jdom=.\resources\lib\jdom.jar
	Set cp=%bin%;%jdom%
	Set sp=.\src

:: create directory for .class files
	If exist %bin% goto EXISTS
	mkdir %bin%
	goto COMPILE
:EXISTS
	echo "%bin% already exists"

:: (re)compile the game
:COMPILE	
	echo "compiling the game..."
	javac -nowarn -sourcepath %sp% -classpath %cp% %main%\Launcher.java %main%\QuickLauncher.java -d %bin%

:: (re)compile the AI classes inside the game
	echo "compiling the AI classes inside the game..."
	For /d %%f In (%aig%\*) Do 
		javac -nowarn -sourcepath %sp% -classpath %cp% %%f\*.java -d %bin%

:: (re)compile the AI classes located in resources\ai
	echo "compiling the AI classes located in the resources..."
	For /d %%f In (%ai%\*) Do 
		echo "    %%f";
		For /d %%g In (%%f\*) Do 
			echo "        %%g";
			javac -nowarn -sourcepath %sp%;%ai% -classpath %cp%;%ai% %%g\*.java

	pause

	Endlocal
