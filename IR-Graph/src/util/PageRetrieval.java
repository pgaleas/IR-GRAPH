package util;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import models.Page;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 * Clase que permite la obtencion de los documentos desde su origen.
 * 
 * @author Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version 1.0
 * @since 1.0
 *
 */

public class PageRetrieval {

	/**
	 * Obtiene los documentos y los deja en como contenidos brutos, en formato raw.
	 * 
	 * TODO añadir libreria apache web client, con timeout.
	 * 
	 * 
	 * @param page
	 */
	public static void retrieveContentPage(Page page){
        try
        {
        	ContentHandler handler = new BodyContentHandler(10*1024*1024);
        	AutoDetectParser parser = new AutoDetectParser();
        	Metadata metadata = new Metadata();
        	ParseContext context = new ParseContext();
        	
        	InputStream stream;
        	/**
        	 * Soporte para https
        	 */
        	if (page.getUrl().startsWith("https"))
        	{
        		URL link = new URL(page.getUrl());
        		HttpsURLConnection con = (HttpsURLConnection)link.openConnection();
        		stream = con.getInputStream();
        	}
        	else
        	{
        		stream = new URL(page.getUrl()).openStream();
        	}
        	parser.parse(stream, handler, metadata,context);
        	
        	String text = handler.toString().replaceAll("\t", " ").replaceAll("\n"," ").replaceAll(" +", " ");
        	page.setContent(text);
        }
        catch(Exception e)
        {
        	System.err.println("ERROR tratando de descargar: " + page.getUrl());
        	e.printStackTrace();
        }
	}

}