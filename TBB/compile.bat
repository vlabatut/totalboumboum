:: -------------------------------------------------------------------
::
:: this is a MS-DOS script, it works only on Windows operating systems.
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
mkdir %bin%

:: (re)compile the game
javac -sourcepath %sp% -classpath %cp% %main%\Launcher.java -d %bin%
:: (re)compile the AI class inside the game
For /d %%f In (%aig%\*) Do javac -sourcepath %sp% -classpath %cp% %%f\*.java -d %bin%

:: (re)compile the AIs located in resources\ai
For /d %%f In (%ai%\*) Do For /d %%g In (%%f\*) Do javac -sourcepath %sp%;%ai% -classpath %cp%;%ai% %%g\*.java

pause

Endlocal
