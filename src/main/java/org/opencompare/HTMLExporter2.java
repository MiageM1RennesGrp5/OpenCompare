package org.opencompare;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMExporter;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gbecan on 13/10/14.
 */
public class HTMLExporter2 implements PCMVisitor, PCMExporter {

	private Boolean renverser = false;
	private Document doc;
    private Element body;
    private Element head;
    private PCMMetadata metadata;
 
    int i = 0;
    int x = 0;
    private Element tr; //Current column
    Document.OutputSettings settings = new Document.OutputSettings();
    private String templateFull = "<html>\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"utf-8\"/>\n" +
            "\t\t<title></title>\n" +
            "\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/stylePerso.css\">\n"+
            "\t\t<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n"+
            "\t\t<script src=\"js/jquery.min.js\"></script>\n"+
            "\t\t<script src=\"js/bootstrap.min.js\"></script>\n"+    
                 
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
    	
       // body.appendElement("h1").text(pcm.getName());
       if (renverser) {
		Element script = body.appendElement("script");
		script.attr("src","js/renverser.js");
       }
        Element title = body.appendElement("h1");
        title.attr("id", "title").text(pcm.getName());
        Element section = body.appendElement("section");
        section.addClass("table-responsive");
        Element table = section.appendElement("table");
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
        tr.appendElement("td").attr("rowspan", Integer.toString(featureDepth)).text("Product");
       
     
        	
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
        	
			this.renverser(table);
			//System.out.println(table.childNodes());
        	
        }
        
       
       
   

    @Override
    public void visit(Feature feature) {
    
    		/*code original*/
    		Element td = tr.appendElement("td");
    		if (featureDepth > 1) {
    			td.attr("rowspan", Integer.toString(featureDepth));
    			
    		}    		
    		td.text(feature.getName());
    		td.attr("class", "en-tete-caracteristiques"+" "+ x);
    		
    	x++;	
    	
    }

    /*Rien change ici*/
    @Override
    public void visit(FeatureGroup featureGroup) {
        Element th = tr.appendElement("th");
        
        if (!featureGroup.getFeatures().isEmpty()) {
            th.attr("colspan", Integer.toString(featureGroup.getFeatures().size()));
        }
        th.text(featureGroup.getName());
        
        //System.out.println("::::::::::::::::::::::::::::"+featureGroup.getName());
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
		
		return cells;}
    @Override
    public void visit(Product product) {
    
  			/*code original*/
			 Element td = tr.appendElement("td");
		        if (featureDepth > 1) {
		        	td.attr("rowspan", Integer.toString(featureDepth));
		        	
		        }
		        td.text(product.getName());
		        td.attr("class","en-tete-produits");
		        List<Cell> cells = product.getCells();

		        Collections.sort(cells, new Comparator<Cell>() {
		            @Override
		            public int compare(Cell cell1, Cell cell2) {
		            	
		                return metadata.getSortedFeatures().indexOf(cell1.getFeature()) - metadata.getSortedFeatures().indexOf(cell2.getFeature());
		            }
		        });

		        for (Cell cell : cells) {
		        	
		            Element td1 = tr.appendElement("td");
		            td1.text(cell.getContent());
		            td1.attr("class",""+i);
		            i++;
		        }
			
		i = 0;
      

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
    
    
   public void renverser(Element table){
	   Element newtable = body.appendElement("table");
	   Element newtr = newtable.appendElement("tr");
	  
	   String[] nouveauContenu = {};
//	   System.out.println(contenu);
//	   System.out.println(contenu.contains("<tr"));
//	   contenu.split("");
	   String contenu = table.toString();
		Pattern pattern = Pattern.compile("<.*>(.*)<.*>");
		Pattern testP = Pattern.compile("<.*>(.*)");
		Matcher m2 = pattern.matcher(contenu);
		Matcher m3;
		 
		
		
	
	  String[] tableTr = contenu.split("</tr>"); 
	  
	  int i = 0;
	  
	  for(String trS : tableTr){
		 
		  
		  
		String[] test3 = trS.split("</td>", 2);
		m3 = testP.matcher(test3[0]);
		while(m3.find()) {
			if (!m3.group(1).equals("")) {
				  Element td = newtr.appendElement("td");
				  td.text(m3.group(1));
				  td.addClass("en-tete-produits");
			}
			
		  }
		
		for (int j = 0; j < test3.length; j++) {

			m3 = pattern.matcher(test3[j]);
			while(m3.find()){
				System.out.println(m3.group(1));
				
			}
		
			
			
		}
				  
	}
		
		 
		 
		  
	  
	  
	 
	System.out.println(newtable.toString());
	   
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


	

	public void setRenverser(Boolean renverser) {
		this.renverser = renverser;
	}
    
    
}
