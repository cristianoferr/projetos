@echo off
java - jar ..\..\..\..\yuicompressor-2.4.8.jar ..\..\..\..\..\build\minified.css -o minified-min.css
del exec.bat
