#!/bin/sh
# -------------------------------------------------------------------
#
# this is a sh script, it should work on most Linux, Unix, and Mac OS
# it (re)compiles the whole TBB game
#
# -------------------------------------------------------------------

# define path variables
main="./src/fr/free/totalboumboum"
aig="${main}/ai"
ai="./resources/ai"
bin="./bin"
jdom="./resources/lib/jdom.jar"
cp="${bin}:${jdom}"
sp="./src"

# create directory for .class files
mkdir ./bin

# (re)compile the game (excepted the AIs located in resources/ai)
# javac -sourcepath ./src -classpath ./bin:./resources/lib/jdom.jar:./resources/ai src/fr/free/totalboumboum/Launcher.java -d bin

# (re)compile the game
javac -sourcepath $sp -classpath $cp $main/Launcher.java -d $bin
# (re)compile the AI classes inside the game
For /d %%f In (%aig%\*) Do javac -sourcepath %sp% -classpath %cp% %%f\*.java -d %bin%

for $filer in ls ; do
  ....
done

for i in $(ls -d */); do echo ${i%%/}; done

# (re)compile the AI classes located in resources\ai
For /d %%f In (%ai%\*) Do For /d %%g In (%%f\*) Do javac -sourcepath %sp%;%ai% -classpath %cp%;%ai% %%g\*.java

# (re)compile the AIs located in resources/ai
