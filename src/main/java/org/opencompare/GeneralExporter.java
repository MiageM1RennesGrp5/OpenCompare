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
	private HTMLExporterRenverse htmlExporterRenverse;
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
		Boolean renverser = Boolean.parseBoolean(properties.getProperty("renverser"));
		if (renverser) {
			this.htmlExporterRenverse = new HTMLExporterRenverse(fileConf, filePcm);
			htmlExporterRenverse.creerFichier(nomFichier, htmlExporterRenverse.toHTML(filePcm));
		}else{
			this.htmlExporter = new HTMLExporter(fileConf,filePcm);
			htmlExporter.creerFichier(nomFichier, htmlExporter.toHTML(filePcm));
		}
		
	}
	
	
	public void exporterCss() throws IOException{
		this.cssExporter = new CSSExporter(fileConf);
		cssExporter.generateCSS();
		cssExporter.creerFichier();
	}
	
}