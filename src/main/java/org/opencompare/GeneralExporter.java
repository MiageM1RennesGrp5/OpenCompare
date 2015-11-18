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
			this.exporterHtmlRenverser("htmlRenverse.html");
		}else{
			
			this.exporterHtml("fichierHTML.html");
		}
		
	}
	
	public void exporterHtml(String nomFichier){
		this.htmlExporter = new HTMLExporter();
		//htmlExporter.setRenverser(Boolean.parseBoolean(properties.getProperty("renverser")));
		htmlExporter.creerFichier(nomFichier, htmlExporter.toHTML(filePcm));
	}
	
	public void exporterHtmlRenverser(String nomFichier){
		this.htmlRenverse = new HTMLExporterRenverse();
		htmlRenverse.creerFichier(nomFichier, htmlRenverse.toHTML(filePcm));
	}
	public void exporterCss(String nomFichier){
		this.cssExporter = new CSSExporter();
	}
	
}
