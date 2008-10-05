:: -------------------------------------------------------------------
::
:: this is a MS-DOS script, it works only on Windows operating systems.
:: it launches TBB, but you can preferably use TBB.exe
::
:: -------------------------------------------------------------------

Setlocal

:: define path variables
Set ai=.\resources\ai
Set bin=.\bin
Set jdom=.\resources\lib\jdom.jar
Set cp=%bin%;%jdom%;%ai%
Set launcher=fr.free.totalboumboum.Launcher
Set splash=resources\gui\images\splash.jpg

java -Xmx128m -splash:%splash% -classpath %cp% %launcher%

:: pause

Endlocal
