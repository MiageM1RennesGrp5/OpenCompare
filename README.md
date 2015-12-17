# OpenCompare - HTMLExporter configurable
Examples for using OpenCompare API and services

1) How to add a matrice to the application ?

  You have to add a .pcm file in the "pcms" folder first. After, in the file named ".\src\main\java\org\opencompare\main.java"     you advice the path to your pcm file in the parameter of the line "File pcmFile = new File("pcms/casqueAudio.pcm");",
  example : "pcms/casqueAudio.pcm"
  
2) Configuration file "configuration.properties"

  This is the configuration file. If you open it, you will find each parameter that you can change to modify the result you want   to obtain when you process the application. Parameters are classify to separate each function of the matrice. In example, you    have a "coloring all table text" paragraph where you can change the color or the style of the text of the matrice you will       get.
