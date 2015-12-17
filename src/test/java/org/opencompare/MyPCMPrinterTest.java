package org.opencompare;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import static org.junit.Assert.*;

import org.opencompare.api.java.io.PCMLoader;
import org.w3c.dom.html.HTMLElement;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlHeader;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;

import org.junit.Test;

import java.awt.List;
import java.io.File;
import java.io.IOException;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinterTest {

      
   


    
    @Test
    public void homePage() throws Exception {
      
        try (final WebClient webClient2 = new WebClient()) {
        	
        	//Correspond au Lien (URL) de fichier local, modifier pour faire le test
            HtmlPage page = webClient2.getPage("file:///C:/Users/sandraisabel/Documents/GitHub/OpenCompare/exports/casqueAudio.html");
            assertEquals("ok ","Comparateur Casque Audio | Trouvez le casque qu'il vous faut !", page.getTitleText());

     

            final String pageAsXml = page.asXml();
           assertTrue(pageAsXml.contains(" <section class=\"table-responsive\">"));
           assertTrue(pageAsXml.contains("  <th class=\"en-tete-caracteristiques\" id=\"Type\">"));
           assertTrue(pageAsXml.contains("<th class=\"en-tete-produits\">"));
         
           assertTrue(pageAsXml.contains("<table class=\"table table-bordered\" id=\"matrix_550097395\" border=\"1\">"));
   

          final String pageAsText = page.asText();
           assertTrue(pageAsText.contains("oui"));
           

       
           final HtmlHead head=(HtmlHead) page.getByXPath("//head").get(0);
           System.out.println(head.asXml());
           assertTrue(head.asXml().contains("<title>"));
           assertTrue(head.asXml().contains("</title>"));
           
           final HtmlBody body = (HtmlBody) page.getByXPath("//body").get(0);
           
           System.out.println(head.asXml());
           assertTrue(body.asXml().contains("<section class=\"table-responsive\">"));
           assertTrue(body.asXml().contains("<table class=\"table table-bordered\" id=\"matrix_550097395\" border=\"1\">"));
           assertTrue(body.asXml().contains("</table>"));
       
      final  HtmlTable table = (HtmlTable) page.getByXPath("//table").get(0);
       assertTrue(table.asXml().contains("<th class=\"en-tete-caracteristiques"));
       assertTrue(table.asXml().contains(" <th class=\"en-tete-produits\">"));
       
       
           
       
            }

    
    
    
    
    
    

}
    }
