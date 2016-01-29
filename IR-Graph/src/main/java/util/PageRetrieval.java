package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import javax.net.ssl.HttpsURLConnection;
import models.Page;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

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
	 */



	private static final Logger logger = Logger.getLogger(PageRetrieval.class);






    public static void retrieveContentPage0(Page page){
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




	public static void retrieveContentPage(Page page, boolean replaceSpecialCharacters){
        try
        {

            //ContentHandler handler = new BodyContentHandler();
			ContentHandler handler = new BodyContentHandler(10*1024*1024);

        	AutoDetectParser parser = new AutoDetectParser();

        	Metadata metadata = new Metadata ();

        	ParseContext context = new ParseContext ();
        	
        	InputStream stream;


        	/**
        	 * Soporte para https
        	 */
        	if (page.getUrl().startsWith("https")) {
        		URL link = new URL(page.getUrl());
        		HttpsURLConnection con = (HttpsURLConnection)link.openConnection();
        		stream = con.getInputStream();
        	}
        	else {
        		stream = new URL(page.getUrl()).openStream();
        	}

        	parser.parse(stream, handler, metadata, context);

			//String text = (replaceSpecialCharacters) ? convertFromUTF8(handler.toString()) : handler.toString();
			String text = handler.toString().replaceAll("\t", " ").replaceAll("\n"," ").replaceAll(" +", " ");

			page.setContent(text);

			System.out.println("URL:"+page.getUrl ());
			System.out.println("TITLE:"+page.getTitle ());
			System.out.println("CONTENT:"+page.getContent ());

        }
        catch(Exception e)
        {
			logger.error("No se pudo descargar el contenido de la página : "+page.getUrl());
        	e.printStackTrace();
        }
	}





    public static void downloadFile(Page page) {
        String fileURL = page.getUrl();


        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = 0;
            responseCode = httpConn.getResponseCode();

            ContentHandler handler = new BodyContentHandler();

            AutoDetectParser parser = new AutoDetectParser ();

            Metadata metadata = new Metadata();

            ParseContext context = new ParseContext();


            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                            fileURL.length());
                }

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();


                try {
                    parser.parse(inputStream, handler, metadata, context);
                } catch (SAXException | TikaException e) {
                    e.printStackTrace ();
                }

                //String text = (replaceSpecialCharacters) ? convertFromUTF8(handler.toString()) : handler.toString();
                String text = handler.toString().replaceAll("\t", " ").replaceAll("\n"," ").replaceAll(" +", " ");

                page.setContent(text);

                System.out.println("Pagina:"+page.getUrl ());
                System.out.println("Pagina:"+page.getTitle ());
                System.out.println("CONTENIDO:"+page.getContent ());

                inputStream.close();

                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();


        } catch (IOException e) {
            e.printStackTrace ();
        }


    }


    public static void downL2(Page page) {

        try {
            HttpGet httpget = new HttpGet (page.getUrl ());
            HttpEntity entity = null;
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute (httpget);
            entity = response.getEntity ();
            if (entity != null) {

                InputStream instream = entity.getContent();

                BodyContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();

                Parser parser = new AutoDetectParser();

                parser.parse (instream, handler, metadata, new ParseContext ());

                String plainText = metadata.toString ();


                System.out.println ("URL:"+page.getUrl ()+"\nContent:"+plainText);

            }
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (SAXException e) {
            e.printStackTrace ();
        } catch (TikaException e) {
            e.printStackTrace ();
        }

    }




    public static void downL3(Page page)  {


        try {
            URL url = new URL(page.getUrl());

            InputStream instream = url.openStream();


            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();

            Parser parser = new AutoDetectParser();

            parser.parse (instream, handler, metadata, new ParseContext ());
            String plainText = handler.toString ();
            System.out.println ("URL:"+page.getUrl ()+"\nContent:"+plainText);


        } catch (IOException e) {
            e.printStackTrace ();
        } catch (SAXException e) {
            e.printStackTrace ();
        } catch (TikaException e) {
            e.printStackTrace ();
        }
    }







	/** Elimina acentos y caracteres especiales del texto extraido de la página
	 * @TODO : falta agregar la eliminación de algunos caracteres especiales que estan aun en UTF8
	 *
	 */
	private static String replaceEspecialCharacters(String text) {
		 if ( text == null) return null;
		 else {
			return Normalizer.normalize(text, Normalizer.Form.NFKD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
					.replaceAll("[\\s-]+", " ")
					.replaceAll("(\\r|\\n|\\t)", " ");
		}
	}


    // convert from UTF-8 -> internal Java String format
    private static String convertFromUTF8(String s) {
        String out;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out.replaceAll("(\\r|\\n|\\t)", " ");
    }


	public static void main(String[] args) {

		Page[] pages = new Page[]{
				new Page ("http://www.ufro.cl", "Universidad de La Frontera", "Este es la Sufro"),
				new Page ("http://www.uach.cl", "Universidad Austral de Chile", "Esta es la UACH"),
				new Page ("http://www.udec.cl", "Universidad de Concepción", "Esta es la U de Conce"),
				new Page ("http://www.ubiobio.cl", "Universidad del Bio Bio", "Este es la UBB")};

		for (int i = 0; i < pages.length; i++) {
            PageRetrieval.retrieveContentPage0 (pages[i]);
        }

		for (int j = 0; j < pages.length; j++) {
			System.out.println (pages[j].getTitle());
			System.out.println (pages[j].getContent());
		}


	}

}