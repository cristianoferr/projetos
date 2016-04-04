@echo off
cls
rem ### Meus projetos...
set CP=%CLASSPATH%
set CP=%CP%;..\GMPlayer\bin;bin
set CP=%CP%;..\GMAssets\bin
set CP=%CP%;..\BluePrintOutput\bin
set CP=%CP%;..\BluePrintManager\bin
set CP=%CP%;..\..\CodigosUteis\JME3Utils\bin
set CP=%CP%;..\..\CodigosUteis\Common\bin
set CP=%CP%;..\..\CodigosUteis\ProceduralUtils\bin
set CP=%CP%;..\..\CodigosUteis\JMonkeyTonegod\bin

rem ### JMonkey
set CP=%CP%;..\..\lib\jmonkey\jme3-core-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-desktop-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-lwjgl-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-plugins-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-effects-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-niftygui-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jME3-bullet-natives.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-jbullet-3.1.0-snapshot-github.jar
set CP=%CP%;..\..\lib\jmonkey\jme3-terrain-3.1.0-snapshot-github.jar

set CP=%CP%;..\..\lib\jmonkey\plugins\jME3-ai.jar

set CP=%CP%;..\..\lib\jmonkey\libs\lwjgl-2.9.1.jar
set CP=%CP%;..\..\lib\jmonkey\libs\nifty-1.3.3.jar
set CP=%CP%;..\..\lib\jmonkey\libs\nifty-default-controls-1.3.3.jar
set CP=%CP%;..\..\lib\jmonkey\libs\nifty-style-black-1.3.3.jar
set CP=%CP%;..\..\lib\jmonkey\libs\eventbus-1.4.jar
set CP=%CP%;..\..\lib\jmonkey\libs\jinput-2.0.5.jar
set CP=%CP%;..\..\lib\jmonkey\libs\xpp3-1.1.4c.jar
set CP=%CP%;..\..\lib\jmonkey\libs\jbullet.jar
set CP=%CP%;..\..\lib\jmonkey\libs\vecmath-1.3.1.jar

rem ### Demais libs
set CP=%CP%;..\..\lib\json\json-simple-1.1.1.jar
set CP=%CP%;..\..\lib\imgscalr\imgscalr-lib-4.2.jar

java -classpath %CP% com.cristiano.java.gm.visualizadores.GMBuilder %1 %2 %3 %4 %5 %6 %7 %8
