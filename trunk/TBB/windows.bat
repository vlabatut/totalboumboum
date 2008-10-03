REM WARNING: compile only the game, and not the AIs (located in resources\ai)
REM javac -sourcepath src -classpath .\bin;.\resources\lib\jdom.jar;.\resources\ai src\fr\free\totalboumboum\Launcher.java -d bin

REM launch the game
java -Xmx128m -classpath .\bin;.\resources\lib\jdom.jar;.\resources\ai fr.free.totalboumboum.Launcher

REM pause