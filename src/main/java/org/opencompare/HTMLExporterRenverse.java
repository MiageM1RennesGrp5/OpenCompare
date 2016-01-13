package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.io.PCMExporter;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.BooleanValue;
import org.opencompare.api.java.value.Conditional;
import org.opencompare.api.java.value.DateValue;
import org.opencompare.api.java.value.Dimension;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.NotApplicable;
import org.opencompare.api.java.value.NotAvailable;
import org.opencompare.api.java.value.Partial;
import org.opencompare.api.java.value.RealValue;
import org.opencompare.api.java.value.StringValue;
import org.opencompare.api.java.value.Unit;
import org.opencompare.api.java.value.Version;

public class HTMLExporterRenverse implements PCMVisitor, PCMExporter{

	private String matrice[][];
	private String matriceInverse[][];
	private Document doc;
	private Element body;
	private Element section;
	private PCMMetadata metadata;
	private Element tr; // Current column
	Document.OutputSettings settings = new Document.OutputSettings();
	private String templateFull = "<html>\n" + "\t<head>\n" + "\t\t<meta charset=\"utf-8\"/>\n"
			+ "\t\t<title></title>\n"
			+ "\t\t<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n"
			+ "\t\t<script src=\"js/jquery.min.js\"></script>\n" + "\t\t<script src=\"js/bootstrap.min.js\"></script>\n"
			+

	"\t</head>\n" + "\t<body>\n" +

	"\t</body>\n" + "</html>";

	private LinkedList<AbstractFeature> nextFeaturesToVisit;
	private int featureDepth;
	private File fileConf;
	private Properties properties;
	FileReader fr;
	
	public HTMLExporterRenverse(File fileConf, PCM pcm) throws IOException {
		int x = pcm.getFeatures().size() + 1;
		int y = pcm.getProducts().size() + 1;
		this.matrice = new String[y][x];
		this.matriceInverse = new String[x][y];
		this.fileConf = fileConf;
		this.properties = new Properties();
		fr = new FileReader(fileConf);
		try {
			properties.load(fr);
		} finally {
			fr.close();
		}
	}

	@Override
	public String export(PCMContainer container) {
		return toHTML(container);
	}

	public String toHTML(PCM pcm) {
		settings.prettyPrint();
		this.doc = Jsoup.parse(templateFull);
		this.body = doc.body();

		doc.head().select("title").first().text(pcm.getName());
		doc.head().append("\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/"+properties.getProperty("nameCSSFile")+".css\">\n");
		if (metadata == null) {
			metadata = new PCMMetadata(pcm);
		}
		pcm.accept(this);
		return doc.outputSettings(settings).outerHtml();

	}
	
	private String toHTML(PCMContainer container) {
		metadata = container.getMetadata();
		return toHTML(container.getPcm());
	}

	@Override
	public void visit(PCM pcm) {
		Element title = body.appendElement("h1");
		title.attr("id", "title").text(pcm.getName());
		section = body.appendElement("section");
		section.addClass("table-responsive");
		
		this.createMatice(pcm);
		
		this.createMatriceInverse();
		this.renverser(pcm);
		
	}
	
	public void createMatice(PCM pcm) {

		// Compute depth
		featureDepth = pcm.getFeaturesDepth();

		// Generate HTML code for features
		LinkedList<AbstractFeature> featuresToVisit;
		featuresToVisit = new LinkedList<>();
		nextFeaturesToVisit = new LinkedList<>();
		featuresToVisit.addAll(pcm.getFeatures());

		matrice[0][0] = "Caracteristiques";

		while (!featuresToVisit.isEmpty()) {
			Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
				@Override
				public int compare(AbstractFeature feat1, AbstractFeature feat2) {
					return metadata.getFeaturePosition(feat1) - metadata.getFeaturePosition(feat2);
				}
			});
			int c1 = 1;
			for (AbstractFeature feature : featuresToVisit) {
				matrice[0][c1] = feature.getName();
				c1++;
			}
			featuresToVisit = nextFeaturesToVisit;
			nextFeaturesToVisit = new LinkedList<>();
			featureDepth--;

		}

		int l = 1;

		for (Product product : pcm.getProducts()) {
			int c = 1;
			matrice[l][0] = product.getName();

			for (Cell cell : this.cellProduct(product)) {

				for (int i = l; i < matrice.length; i++) {
					for (int j = c; j < matrice[i].length; j++) {
						matrice[i][j] = cell.getContent();
					}
				}
				c++;

			}
			l++;

		}
	}

	public void createMatriceInverse() {
		for (int x = 0; x < matrice.length; x++) {
			for (int y = 0; y < matrice[x].length; y++) {
				matriceInverse[y][x] = matrice[x][y];

			}
		}
	}

	/**
	 * Retunr l'element table renversé
	 * 
	 * @return Element table
	 */
	public Element renverser(PCM pcm) {
		List<String> listeStr = Arrays.asList("true", "yes", "oui");
		boolean colorierInterval = Boolean.parseBoolean(properties.getProperty("coloringNumericalRange"));
		boolean colorierBoolean = Boolean.parseBoolean(properties.getProperty("coloringBooleanValues"));
		boolean colorierNegatifsPositifs = Boolean.parseBoolean(properties.getProperty("colorigPositiveNegativeValues"));
		String caracteristiqueInterval = "";
		String caracteristiqueBoolean = "";
		String caracteristiquePosNeg = "";
		int valMin = 0;
		int valMax = 0;
		if (colorierInterval) {
			caracteristiqueInterval = properties.getProperty("numericalFeatureName");
			valMin = Integer.parseInt(properties.getProperty("minValue"));
			valMax = Integer.parseInt(properties.getProperty("maxValue"));
		}
		if (colorierBoolean) {
			caracteristiqueBoolean = properties.getProperty("booleanFeatureName");
		}
		if (colorierNegatifsPositifs) {
			caracteristiquePosNeg = properties.getProperty("positiveNegativeFeatureName");
		}
		
		Element table = section.appendElement("table");
		table.addClass("table table-bordered");
		table.attr("id", "matrix_" + pcm.getName().hashCode()).attr("border", "1");
		Element td;
		Element th;
		String caracteristiqueCurrent = "";
		float valeurNumeriqueCurrent = 0;
		String valeurBoleanCurrent = "";
		for (int i = 0; i < matriceInverse.length; i++) {
			tr = table.appendElement("tr");
			for (int j = 0; j < matriceInverse[i].length; j++) {
				if (i == 0 || j == 0) {
					th = tr.appendElement("th");
					th.text(matriceInverse[i][j]);
					if(j == 0){
						th.addClass("en-tete-caracteristiques");
						th.attr("id",matriceInverse[i][j] );
						caracteristiqueCurrent = matriceInverse[i][j];
					}
					if(i == 0){
						th.addClass("en-tete-produits");
					}
				}else{
					td = tr.appendElement("td");
					td.text(matriceInverse[i][j]);
					if (colorierInterval && caracteristiqueCurrent.equals(caracteristiqueInterval)) {
						try {
							valeurNumeriqueCurrent = Integer.parseInt(matriceInverse[i][j]);
							
							if (valeurNumeriqueCurrent <= valMax && valeurNumeriqueCurrent >= valMin) {
								td.attr("class", "colorierNumerique");
							}
							
						} catch (Exception e) {
							System.out.println("La casse ne contient pas une valeur numerique valable");
						}
							
					}
					if (colorierBoolean && caracteristiqueCurrent.equals(caracteristiqueBoolean)) {
						valeurBoleanCurrent = matriceInverse[i][j];
						valeurBoleanCurrent.toLowerCase();
						if (listeStr.contains(valeurBoleanCurrent)) {
							td.attr("class", "success");
						} else {
							td.attr("class", "danger");
						}
					}
					if (colorierNegatifsPositifs && caracteristiquePosNeg.equals(caracteristiqueCurrent)) {
						try {
							valeurNumeriqueCurrent = Float.parseFloat(matriceInverse[i][j].replace(",", "."));
							if (valeurNumeriqueCurrent < 0) {
								td.attr("class", "danger");
							}else{
								td.attr("class", "success");
							}
						} catch (Exception e) {
							System.out.println("La casse ne contient pas une valeur numerique valable");
						}
						
					}
					
					
				}
				
			}
		}
		
		return table;
	}


	@Override
	public void visit(Feature feature) {

		Element th = tr.appendElement("th");
		if (featureDepth > 1) {
			th.attr("rowspan", Integer.toString(featureDepth));

		}
		th.text(feature.getName());
	}

	@Override
	public void visit(FeatureGroup featureGroup) {
		Element th = tr.appendElement("th");

		if (!featureGroup.getFeatures().isEmpty()) {
			th.attr("colspan", Integer.toString(featureGroup.getFeatures().size()));
		}
		th.text(featureGroup.getName());

		nextFeaturesToVisit.addAll(featureGroup.getFeatures());
	}

	public List<Cell> cellProduct(Product product) {

		List<Cell> cells = product.getCells();

		Collections.sort(cells, new Comparator<Cell>() {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return metadata.getSortedFeatures().indexOf(cell1.getFeature())
						- metadata.getSortedFeatures().indexOf(cell2.getFeature());
			}
		});

		return cells;
	}

	@Override
	public void visit(Product product) {

		Element th = tr.appendElement("th");
		if (featureDepth > 1) {
			th.attr("rowspan", Integer.toString(featureDepth));

		}
		th.text(product.getName());

		List<Cell> cells = product.getCells();

		Collections.sort(cells, new Comparator<Cell>() {
			@Override
			public int compare(Cell cell1, Cell cell2) {

				return metadata.getSortedFeatures().indexOf(cell1.getFeature())
						- metadata.getSortedFeatures().indexOf(cell2.getFeature());
			}
		});

		for (Cell cell : cells) {

			Element td = tr.appendElement("td");
			td.text(cell.getContent());
		}

	}
	
	/**
	 * creation du fichier html
	 */
	public void creerFichier(String nomFichier, String contenu) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("exports/" + nomFichier)));
			writer.write(contenu);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Cell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BooleanValue booleanValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Conditional conditional) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntegerValue integerValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiple multiple) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NotApplicable notApplicable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NotAvailable notAvailable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Partial partial) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RealValue realValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringValue stringValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Unit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Version version) {
		// TODO Auto-generated method stub
		
	}

}
