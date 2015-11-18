package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.Document;

public class CSSExporter {

	private Document doc;
	/**
     * creation du fichier css
     */
    public void creerFichier(String nomFichier, String contenu){
    	 try {
	    	   BufferedWriter writer = new BufferedWriter(new FileWriter(new File("exports/"+nomFichier)));
	    	   writer.write(contenu);
	    	    
	    	   writer.close();
	    	   }
	    	   catch (IOException e)
	    	   {
	    	   e.printStackTrace();
	    	   }
    }
	
}
