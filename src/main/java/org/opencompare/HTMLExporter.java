package org.opencompare;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.opencompare.api.java.*;
import org.opencompare.api.java.io.PCMExporter;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by gbecan on 13/10/14.
 */
public class HTMLExporter implements PCMVisitor, PCMExporter {

	private boolean renverser = true; //savoir s'il faut renverser ou pas
	
	private Document doc;
    private Element body;
    private PCMMetadata metadata;
    private Element tr; //Current column
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
    public HTMLExporter() {
    	
		
    	// TODO Auto-generated constructor stub
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
        table.attr("id", "matrix_" + pcm.getName().hashCode()).attr("border", "1");

        // Compute depth
        featureDepth = pcm.getFeaturesDepth();

        // Generate HTML code for features
        LinkedList<AbstractFeature> featuresToVisit;
        featuresToVisit = new LinkedList<>();
        nextFeaturesToVisit = new LinkedList<>();
        featuresToVisit.addAll(pcm.getFeatures());

        tr = table.appendElement("tr");
        tr.appendElement("th").attr("rowspan", Integer.toString(featureDepth)).text("Product");
        
        if(renverser){
        	/*Code de base modifie*/
        	
        	 // Generate HTML code for products
            for (Product product : pcm.getProducts()) {
            	//tr.appendElement("th");
                //tr = table.appendElement("tr");
                product.accept(this);
            }
            while(!featuresToVisit.isEmpty()) {
                Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
                    @Override
                    public int compare(AbstractFeature feat1, AbstractFeature feat2) {
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
        	
        }else{
        	
        	/*Code original*/
        	while (!featuresToVisit.isEmpty()) {
				Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
					@Override
					public int compare(AbstractFeature feat1, AbstractFeature feat2) {
						return metadata.getFeaturePosition(feat1) - metadata.getFeaturePosition(feat2);
					}
				});
				for (AbstractFeature feature : featuresToVisit) {
					
					feature.accept(this);
				}
				featuresToVisit = nextFeaturesToVisit;
				nextFeaturesToVisit = new LinkedList<>();
				featureDepth--;
				if (featureDepth >= 1) {
					tr = table.appendElement("tr");
				}
			}
			// Generate HTML code for products
			for (Product product : pcm.getProducts()) {
				tr = table.appendElement("tr");
				product.accept(this);
			}
        	
        	
        }
        
       
       
    }

    @Override
    public void visit(Feature feature) {
    	
    	if(renverser){
    		/*code de base modifie*/
    		
    		tr.appendElement("th").text(feature.getName());
    		List<Cell> cells = feature.getCells();
    		
    		Collections.sort(cells, new Comparator<Cell>() {
    			@Override
    			public int compare(Cell cell1, Cell cell2) {
    				
    				return metadata.getSortedFeatures().indexOf(cell2.getFeature())
    						- metadata.getSortedFeatures().indexOf(cell1.getFeature());
//    				return metadata.getSortedProducts().indexOf(cell1.getProduct())
//    						- metadata.getSortedProducts().indexOf(cell2.getProduct());
    			}
    		});
    		for (Cell cell : cells) {
    			Element td = tr.appendElement("td");
    			td.appendElement("span").text(cell.getContent());
    		}
    		
    		
    	}else{
    		
    		/*code original*/
    		Element th = tr.appendElement("th");
    		if (featureDepth > 1) {
    			th.attr("rowspan", Integer.toString(featureDepth));
    		}
    		th.text(feature.getName());
    		
    	}
    }

    /*Rien change ici*/
    @Override
    public void visit(FeatureGroup featureGroup) {
        Element th = tr.appendElement("th");
        if (!featureGroup.getFeatures().isEmpty()) {
            th.attr("colspan", Integer.toString(featureGroup.getFeatures().size()));
        }
        th.text(featureGroup.getName());
        nextFeaturesToVisit.addAll(featureGroup.getFeatures());
    }

    @Override
    public void visit(Product product) {
    	
    	if (renverser) {
    		
    		/*code de base modifie*/
    		tr.appendElement("th").text(product.getName());
		} else {

			/*code original*/
			 Element th = tr.appendElement("th");
		        if (featureDepth > 1) {
		        	th.attr("rowspan", Integer.toString(featureDepth));
		        }
		        th.text(product.getName());

		        List<Cell> cells = product.getCells();

		        Collections.sort(cells, new Comparator<Cell>() {
		            @Override
		            public int compare(Cell cell1, Cell cell2) {
		                return metadata.getSortedFeatures().indexOf(cell1.getFeature()) - metadata.getSortedFeatures().indexOf(cell2.getFeature());
		            }
		        });

		        for (Cell cell : cells) {
		            Element td = tr.appendElement("td");
		            td.appendElement("span").text(cell.getContent());
		        }
			
		}
      

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

    }

    @Override
    public void visit(BooleanValue booleanValue) {

    }

    @Override
    public void visit(Conditional conditional) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(Dimension dimension) {

    }

    @Override
    public void visit(IntegerValue integerValue) {

    }

    @Override
    public void visit(Multiple multiple) {

    }

    @Override
    public void visit(NotApplicable notApplicable) {

    }

    @Override
    public void visit(NotAvailable notAvailable) {

    }

    @Override
    public void visit(Partial partial) {

    }

    @Override
    public void visit(RealValue realValue) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }
}
