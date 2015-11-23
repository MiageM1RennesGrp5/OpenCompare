package org.opencompare;

import java.io.File;

import java.io.IOException;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method  stub
		 File pcmFile = new File("pcms/pommes.pcm");
	     PCMLoader loader = new KMFJSONLoader();
	     PCM pcm = loader.load(pcmFile).get(0).getPcm();
	        
	      File configFile = new File("config/configuration.properties");
	         
//	      HTMLExporter htmlExporter = new HTMLExporter();
//	      htmlExporter.creerFichier("fichierHTML.html", htmlExporter.toHTML(pcm));
	      
	      
	       GeneralExporter exporter = new GeneralExporter(pcm, configFile);
	      
	        
	}

}
