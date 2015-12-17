# OpenCompare - HTMLExporter configurable
Examples for using OpenCompare API and services

1) How to add a matrice to the application ?

  You have to add a .pcm file in the "pcms" folder first. After, in the file named ".\src\main\java\org\opencompare\main.java"     you advice the path to your pcm file in the parameter of the line "File pcmFile = new File("pcms/casqueAudio.pcm");",
  example : "pcms/casqueAudio.pcm"
  
2) Configuration file "configuration.properties"

  This is the configuration file. If you open it, you will find each parameter that you can change to modify the result you want   to obtain when you process the application. Parameters are classify to separate each function of the matrice. In example, you    have a "coloring all table text" paragraph where you can change the color or the style of the text of the matrice you will       get.

3) Test case

  In the src/Test/java package you will find a class named MyPCMPrinterTest.java. If you open it you can see all the tests we      create to test our application. To explain what we've done, we test some page elements, in example the tittle. We and a string   value that should be the name of the page, and we use a function to retrive the name of the page. We compare the two values and   they should be the same. If it's ok, Junit will say that the test went well, if not, Junit will advertise you in RED that there   was a problem in the test execution
