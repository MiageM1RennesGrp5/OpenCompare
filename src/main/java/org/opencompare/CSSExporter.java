package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CSSExporter {

	
	private File config;
	private Properties properties;
	private FileReader fr;
	private String contenuCSS;
	
	public CSSExporter(File config) throws FileNotFoundException{
		this.config = config;
		this.properties = new Properties();
		this.fr = new FileReader(config); 
		this.contenuCSS = "";
	}
	
	public String generateCSS() throws IOException{
		try{
			properties.load(fr);
		}finally{
			fr.close();
		}
		
		//style en tete de produits
		if (Boolean.parseBoolean(properties.getProperty("changeStyleHeaderProducts"))) {
			contenuCSS += ".en-tete-produits{\n";
			
			if (!properties.getProperty("colorHeaderProducts").isEmpty()) {
				contenuCSS += "\t color:"+properties.getProperty("colorHeaderProducts")+";\n";
			}
			if (!properties.getProperty("fontWeightHeaderProducts").isEmpty()) {
				contenuCSS += "\t font-weight:"+properties.getProperty("fontWeightHeaderProducts")+";\n";
			}
			contenuCSS += "}\n";
		}
		
		
		//style en tete de caracteristiques
		if (Boolean.parseBoolean(properties.getProperty("changeStyleHeaderFeatures"))) {
			contenuCSS += ".en-tete-caracteristiques{\n";
			
			if (!properties.getProperty("colorHeaderFeatures").isEmpty()) {
				contenuCSS += "\t color:"+properties.getProperty("colorHeaderFeatures")+";\n";
			}
			if (!properties.getProperty("fontWeightHeaderFeatures").isEmpty()) {
				contenuCSS += "\t font-weight:"+properties.getProperty("fontWeightHeaderFeatures")+";\n";
			}
			
			
			contenuCSS += "}\n";
		}
		
		
		//style colorier intervales numeriques
		if (Boolean.parseBoolean(properties.getProperty("coloringNumericalRange"))) {
			if (!properties.getProperty("colorOfRange").isEmpty()) {
				String couleur = properties.getProperty("colorOfRange");
				contenuCSS +=".colorierNumerique{\n";
				contenuCSS +="\tbackground-color: "+couleur+";\n";
				contenuCSS +="}\n";
			}	
		}
		
		//style de tout le texte de la table
		if (Boolean.parseBoolean(properties.getProperty("changeAllTextStyle"))) {
			contenuCSS +="table{\n";
			if (!properties.getProperty("textColor").isEmpty()) {
				String couleur = properties.getProperty("textColor");
				contenuCSS +="\tcolor: "+couleur+";\n";
			}
			if (!properties.getProperty("fontStyle").isEmpty()) {
				String fontStyle = properties.getProperty("fontStyle");
				contenuCSS +="\tfont-style: "+fontStyle+";\n";
			}
			contenuCSS +="}\n";
		}
		return contenuCSS;
		
	}
	/**
     * creation du fichier css
     */
    public void creerFichier(){
    	 try {
    		
				
    		 if (!properties.getProperty("nameCSSFile").equals("")) {
	    	   BufferedWriter writer = new BufferedWriter(new FileWriter(new File("exports/css/"+properties.getProperty("nameCSSFile")+".css")));
	    	   writer.write(contenuCSS);
	    	    
	    	   writer.close();
    		 }
	    	   }
	    	   catch (IOException e)
	    	   {
	    	   e.printStackTrace();
	    	   }
    	 
    }
	
}
