package org.opencompare;

import java.io.File;

import java.io.IOException;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

public class Main {

	public static void main(String[] args) throws IOException {
		
		//Indiquer le nom du fichier .pcm
		File pcmFile = new File("pcms/casqueAudio.pcm");
	    PCMLoader loader = new KMFJSONLoader();
	    //Creation d'un PCM a partir du fichier choisi
	    PCM pcm = loader.load(pcmFile).get(0).getPcm();
	    //Indiquer le chemin du fichier de configuration
	    File configFile = new File("config/configuration.properties");
	         
	    GeneralExporter exporter = new GeneralExporter(pcm, configFile);
	    String nomFichier = pcmFile.getName();
	    exporter.exporterHtml(nomFichier.substring(0, nomFichier.length()-4));           
	}

}
