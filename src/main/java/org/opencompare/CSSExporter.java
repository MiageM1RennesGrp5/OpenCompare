package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CSSExporter {

	private Document doc;
	Document.OutputSettings settings = new Document.OutputSettings();
	private File config;
	private Properties properties;
	private FileReader fr;
	private String contenuCSS;
	
	public CSSExporter(File config) throws FileNotFoundException{
		this.config = config;
		this.properties = new Properties();
		this.fr = new FileReader(config); 
	}
	
	public String test() throws IOException{
		try{
			properties.load(fr);
		}finally{
			fr.close();
		}
		
		//style en tete de produits
		contenuCSS = ".en-tete-produits{\n";
		
		if (!properties.getProperty("coleurEnTeteProduits").isEmpty()) {
			contenuCSS += "\t color:"+properties.getProperty("coleurEnTeteProduits")+";\n";
		}
		if (!properties.getProperty("fontWeightEnTeteProduits").isEmpty()) {
			contenuCSS += "\t font-weight:"+properties.getProperty("fontWeightEnTeteProduits")+";\n";
		}
		contenuCSS += "}\n";
		
		//style en tete de caracteristiques
		contenuCSS += ".en-tete-caracteristiques{\n";
		
		if (!properties.getProperty("coleurEnTeteCaracteristiques").isEmpty()) {
			contenuCSS += "\t color:"+properties.getProperty("coleurEnTeteCaracteristiques")+";\n";
		}
		if (!properties.getProperty("fontWeightEnTeteCaracteristiques").isEmpty()) {
			contenuCSS += "\t font-weight:"+properties.getProperty("fontWeightEnTeteCaracteristiques")+";\n";
		}
		contenuCSS += "}\n";
		
		System.out.println(contenuCSS);
		return contenuCSS;
	}
	/**
     * creation du fichier css
     */
    public void creerFichier(String nomFichier){
    	 try {
	    	   BufferedWriter writer = new BufferedWriter(new FileWriter(new File("exports/css/"+nomFichier)));
	    	   writer.write(contenuCSS);
	    	    
	    	   writer.close();
	    	   }
	    	   catch (IOException e)
	    	   {
	    	   e.printStackTrace();
	    	   }
    }
	
}
