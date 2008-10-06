#!/bin/sh
# -------------------------------------------------------------------
#
# this is a sh script, it should work on most Linux, Unix, and Mac OS
:: it launches TBB with the appropriate parameters
#
# -------------------------------------------------------------------

# define path variables
ai="./resources/ai"
bin="./bin"
jdom"=./resources/lib/jdom.jar"
cp="${bin}:${jdom}:${ai}"
launcher="fr.free.totalboumboum.Launcher"
splash="resources/gui/images/splash.png"

# launch the game
java -Xmx128m -splash:$splash -classpath $cp $launcher
