package org.opencompare;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.opencompare.api.java.PCM;

public class GeneralExporter {
	
	private PCM filePcm;
	private File fileConf;
	private HTMLExporter htmlExporter;
	private CSSExporter cssExporter;
	private Properties properties;
	private FileReader fr;
	
	
	
	public GeneralExporter(PCM filePcm, File fileConf) throws IOException{
		this.filePcm = filePcm;		
		this.fileConf = fileConf;
		this.readConfigFile(fileConf);
	}
	
	public void readConfigFile(File fileConf) throws IOException{
		properties = new Properties();
		fr = new FileReader(fileConf);
		try {
			properties.load(fr);
		} finally {
			fr.close();
		}
	}
	
	public void exporterHtml(String nomFichier) throws IOException{
		boolean renverser = Boolean.parseBoolean(properties.getProperty("renverser"));
		this.htmlExporter = new HTMLExporter(fileConf,filePcm);
		htmlExporter.setRenverser(renverser);
		htmlExporter.creerFichier(nomFichier, htmlExporter.toHTML(filePcm));
	}
	
	
	public void exporterCss(String nomFichier) throws IOException{
		this.cssExporter = new CSSExporter(fileConf);
		cssExporter.generateCSS();
		cssExporter.creerFichier(nomFichier);
	}
	
}
