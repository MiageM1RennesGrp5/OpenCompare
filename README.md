# OpenCompare - HTMLExporter configurable

<h1> How to add a matrice to the application ?</h1>

You have to add a .pcm file in the "pcms" folder first. After, in the file named ".\src\main\java\org\opencompare\main.java" you advice the path to your pcm file in the parameter of the line "File pcmFile = new File("pcms/casqueAudio.pcm");", 
example : "pcms/casqueAudio.pcm"
  
<h1> Configuration file "configuration.properties"</h1>

This is the configuration file. If you open it, you will find each parameter that you can change to modify the result you want to obtain when you process the application. Parameters are classify to separate each function of the matrice. In example, you have a "coloring all table text" paragraph where you can change the color or the style of the text of the matrice you will get.

<h3>Description of Configuration file</h3>

In this file colors must be expressed in hexadecimal format (#FF0000) or English (red).Don't use ('').

Reverse
<ul>
<li><b>reverse</b>: In this field indicate 'true' if you want an inverse matrice, if not indicate 'false'.</li>
</ul>
Style of products header
<ul>
<li><b>changeStyleHeaderProducts:</b>If you want personalize the style of products header indicate 'true'</li>
<li><b>colorHeaderProducts:</b>In this field indicate the color of text.</li>
<li><b>fontWeightHeaderProducts:</b>Indicate font weight of text (for example 'bold')</li>
</ul>
Style of features header
<ul>
<li><b>changeStyleHeaderFeatures:</b>If you want personalize the style of features header indicate 'true'</li>
<li><b>colorHeaderFeatures:</b>In this field indicate the color of text.</li>
<li><b>fontWeightHeaderFeatures</b>Indicate font weight of text (for exemple 'bold').</li>
</ul>
Coloring by numerical ranges
<ul>
<li><b>coloringNumericalRange:</b>If you want coloring numerical ranges indicate 'true'.</li>
<li><b>numericalFeatureName:</b>Name of feature to coloring. The content of feature must be numeric.</li>
<li><b>minValue:</b>Minimal numerical value.</li>
<li><b>maxValue:</b>Maximal numerical value.</li>
<li><b>colorOfRange:</b>Background color of range selected.</li>
</ul>
Coloring by boolean values
<ul>
<li><b>coloringBooleanValues:</b>If you want coloring boolean values indicate 'true'.</li>
<li><b>booleanFeatureName:</b>Name of feature to coloring.The content of feature must be boolean (example: yes, true, oui).</li>
</ul>
Coloring positive and negative values
<ul>
<li><b>colorigPositiveNegativeValues:</b>If you want coloring positives et negatives values indicate 'true'</li>
<li><b>positiveNegativeFeatureName:</b>Name of feature to coloring. The content of feature must be numeric.</li>
</ul>
Style of table content
*This function don't change the style indicated above...
<ul>
<li><b>changeAllTextStyle:</b>If you want change a table style indicate 'true'.</li>
<li><b>textColor:</b>Color of text.</li>
<li><b>fontStyle:</b>Font style, for example 'italic'.</li>
</ul>
<h1> Test case</h1>

<b>BEFORE:</b> Before testing you need change path HTML file "file:///C:/Users/sandraisabel/Documents/GitHub/OpenCompare/exports/casqueAudio.html", is a local file,
When you open the FILE HTML in a navigator copy the URL and paste to replace.

In the src/Test/java package you will find a class named MyPCMPrinterTest.java. If you open it you can see all the tests we created to test our application. To explain what we've done, we tested some page elements, in example the title. We add a string value that should be the name of the page, and we use a function to retrive the name of the page. We compare the two values and they should be the same. If it's ok, Junit will say that the test went well, if not, Junit will advertise you in RED that there was a problem in the test execution.

<h1> Maven dependencies for tests case</h1>

  You have to add Maven dependencies to run your tests.
  Follow this method : right click on the project folder -> Maven -> add dependency
  <dependency>
    <groupId>net.sourceforge.htmlunit</groupId>
    <artifactId>htmlunit</artifactId>
    <version>2.19</version>
</dependency>

Click OK.

<h1> Technologies used</h1>

- Eclipse
- Java
- Junit
- Maven
- Json
- bootstrap
- HTML5/CSS3
- Javascript


<h1> Licence</h1>

Open Source

<h1>Project structure</h1>

<pre>OpenCompare
  config
    configuration.properties
  exports
    css
      bootstrap.min
      bootstrap-theme.min
      stylePerso.css //CSS exported here
    js
      bootstrap.min
      jquery.min
    {All HTML files exported}
  pcms
    casqueAudio.pcm
    example.pcm
    pommes.pcm
    test.pcm
  src
    main
      java.org.opencompare
        CSSExporter.java
        GeneralExporter.java
        HTMLExporter.java
        HTMLExporterRenverser.java
        Main.java
        MyPCMPrinter.java
    test
      java.org.opencompare
        MyPCMPrinterTest.java //all the tests here
</pre>

<h1>Demo of application</h1>
Click in the next link to see a video demostration of application "Opencompare HTML Exporter Configurable"
https://www.youtube.com/watch?v=vNjkE8g0A8A

<h3>Instructions execution of demo</h3>
<ol>
<li>Indicates your PCM file in the class "main.java"</li>
<li>In the file properties indicate "reverse : false", dont change the others fields. The result of this is the matrix of products not reverse et not personalize.</li>
<li>Execute the class "main.java".</li>
<li>Open your HTML page. "OpenCompare\exports".</li>
<li>Personalize the file "config\configuration.properties" and indicates. The result of this is the matrix of products personalize.</li>
<li>Execute the class main</li>
<li>Open your file HTML to see the result.</li>
<li>Repeat the sames steps but this time indicates 'renverse:true'. The result of this is the matrix of products reverse</li>
<li>You can see the result of export CSS if you open the file 'stylePerso.css'</li>
</ol>
