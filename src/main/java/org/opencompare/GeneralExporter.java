package org.opencompare;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.opencompare.api.java.PCM;

public class GeneralExporter {

	PCM filePcm;
	File fileConf;
	HTMLExporter htmlExporter;
	HTMLExporterRenverse htmlRenverse;
	HTMLExporter2 htmlExporter2;
	CSSExporter cssExporter;
	Properties properties;
	FileReader fr;
	public GeneralExporter(PCM filePcm, File fileConf) throws IOException {
		
		// TODO Auto-generated constructor stub
		this.filePcm = filePcm;
		this.fileConf = fileConf;
		properties = new Properties();
		fr = new FileReader(fileConf);
		try {
			properties.load(fr);
		} finally {
			fr.close();
		}
		
		if (Boolean.parseBoolean(properties.getProperty("renverser"))) {
			//this.exporterHtmlRenverser("htmlRenverse.html");
			
			this.exporterHTMLRenverseJquery("htmlRenverseJquery.html",true);
		}else{
			
			//this.exporterHtml("fichierHTML.html");
			this.exporterHTMLRenverseJquery("htmlJquery.html",false);
			
		}
		this.exporterCss("stylePerso.css");
	}
	
	public void exporterHtml(String nomFichier){
		this.htmlExporter = new HTMLExporter();
		htmlExporter.creerFichier(nomFichier, htmlExporter.toHTML(filePcm));
	}
	
	public void exporterHtmlRenverser(String nomFichier){
		this.htmlRenverse = new HTMLExporterRenverse();
		htmlRenverse.creerFichier(nomFichier, htmlRenverse.toHTML(filePcm));
	}
	public void exporterCss(String nomFichier) throws IOException{
		this.cssExporter = new CSSExporter(fileConf);
		cssExporter.test();
		cssExporter.creerFichier(nomFichier);
	}
	
	
	/**
	 * 
	 */
	public void exporterHTMLRenverseJquery(String nomFichier, Boolean renverser){
		this.htmlExporter2 = new HTMLExporter2();
		htmlExporter2.setRenverser(renverser);
		htmlExporter2.creerFichier(nomFichier, htmlExporter2.toHTML(filePcm));
	}
}
