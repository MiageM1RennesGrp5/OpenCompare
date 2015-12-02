package org.opencompare;

import java.io.File;

import java.io.IOException;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

public class Main {

	public static void main(String[] args) throws IOException {
		 File pcmFile = new File("pcms/casqueAudio.pcm");
	     PCMLoader loader = new KMFJSONLoader();
	     PCM pcm = loader.load(pcmFile).get(0).getPcm();
	     File configFile = new File("config/configuration.properties");
	         
	     GeneralExporter exporter = new GeneralExporter(pcm, configFile);
	     exporter.exporterHtml("test56.html");
	      
	        
	}

}
