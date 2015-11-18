package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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

	private Document doc;
    private Element body;
    private PCMMetadata metadata;
    private Element tr; //Current column
    private Product productTest;
    Document.OutputSettings settings = new Document.OutputSettings();
    private String templateFull = "<html>\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"utf-8\"/>\n" +
            "\t\t<title></title>\n" +
            "\t</head>\n" +
            "\t<body>\n" +
            "\t</body>\n" +
            "</html>";

    private LinkedList<AbstractFeature> nextFeaturesToVisit;
    private int featureDepth;
	
	@Override
	public String export(PCMContainer container) {
		 return toHTML(container);
	}
	
	   
    public String toHTML(PCM pcm) {
        settings.prettyPrint();
        this.doc = Jsoup.parse(templateFull);
    	this.body = doc.body();
       
        doc.head().select("title").first().text(pcm.getName());
        if (metadata == null) {
            metadata = new PCMMetadata(pcm);
        }
        pcm.accept(this);
        return doc.outputSettings(settings).outerHtml();

    }

    public String toHTML(PCMContainer container) {
        metadata = container.getMetadata();
        return toHTML(container.getPcm());
    }

	@Override
	public void visit(PCM pcm) {
		body.appendElement("h1").text(pcm.getName());
        Element title = body.appendElement("h1");
        title.attr("id", "title").text(pcm.getName());
        Element table = body.appendElement("table");
        table.addClass("table table-bordered");
        table.attr("id", "matrix_" + pcm.getName().hashCode()).attr("border", "1");
	
        // Compute depth
        featureDepth = pcm.getFeaturesDepth();

        
        // Generate HTML code for features
        LinkedList<AbstractFeature> featuresToVisit;
        featuresToVisit = new LinkedList<>();
        nextFeaturesToVisit = new LinkedList<>();
        featuresToVisit.addAll(pcm.getFeatures());
        
 

        tr = table.appendElement("tr");
        tr.appendElement("th").attr("rowspan", Integer.toString(featureDepth)).text("Caracteristiques");
	
        /*Code de base modifie*/
    	
   	 // Genserate HTML code for products
       for (Product product : pcm.getProducts()) {
       	//tr.appendElement("th");
           //tr = table.appendElement("tr");
    	   productTest = product;
       	//cellProduct(product);
          product.accept(this);
       }
       
       while(!featuresToVisit.isEmpty()) {
           Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
             
			@Override
			public int compare(AbstractFeature feat1, AbstractFeature feat2) {
				// TODO Auto-generated method stub
				return metadata.getFeaturePosition(feat1) - metadata.getFeaturePosition(feat2);
			}
           });
           for (AbstractFeature feature : featuresToVisit) {
           	tr = table.appendElement("tr");
               feature.accept(this);
           }
           featuresToVisit = nextFeaturesToVisit;
           nextFeaturesToVisit = new LinkedList<>();
           featureDepth--;
           if (featureDepth >= 1) {
               tr = table.appendElement("tr");
           }
       }
   	
	}

	@Override
	public void visit(Feature feature) {
		/*code de base modifie*/
		
		tr.appendElement("th").text(feature.getName());
		List<Cell> cells = feature.getCells();
		
		 Collections.sort(cells, new Comparator<Cell>() {
	            @Override
	            public int compare(Cell cell1, Cell cell2) {
	            	
	                return metadata.getSortedFeatures().indexOf(cell1.getFeature()) - metadata.getSortedFeatures().indexOf(cell2.getFeature());
	            }
	        });
		
		for (Cell cell : cells) {
			
				Element td = tr.appendElement("td");
				td.appendElement("span").text(cell.getContent());
			
				//System.out.println(cell.getContent());
			
		}
		
	}

	@Override
	 public void visit(FeatureGroup featureGroup) {
        Element th = tr.appendElement("th");
        if (!featureGroup.getFeatures().isEmpty()) {
            th.attr("colspan", Integer.toString(featureGroup.getFeatures().size()));
        }
        th.text(featureGroup.getName());
        System.out.println("::::::::::::::::::::::::::::"+featureGroup.getName());
        nextFeaturesToVisit.addAll(featureGroup.getFeatures());
    }
	
	public List<Cell> cellProduct(Product product){
    	List<Cell> cells = product.getCells();
		
		Collections.sort(cells, new Comparator<Cell>() {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return metadata.getSortedFeatures().indexOf(cell1.getFeature())
						- metadata.getSortedFeatures().indexOf(cell2.getFeature());
			}
		});
		for (Cell cell : cells) {
			System.out.println(cell.getContent());
			
		}
		return cells;}

	@Override
	public void visit(Product product) {
		tr.appendElement("th").text(product.getName());
		
	}
	
	 /**
     * creation du fichier html
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
