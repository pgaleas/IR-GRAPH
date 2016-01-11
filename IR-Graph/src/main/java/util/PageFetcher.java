package util;

import models.Page;
import org.apache.log4j.Logger;
import org.apache.lucene.util.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by patricio on 08-01-16.
 */
public class PageFetcher {


    private static final Logger logger = Logger.getLogger(PageFetcher.class);
    private static Page[] rankingPages;
    private static Page[] Group1; // Grupo de paginas de tamaño pequeño
    private static Page[] Group2; // Grupo de paginas de tamaño mediano
    private static Page[] Group3; // Grupo de paginas de tamaño gande

    public static void fetchRankingPages(Page[] pages) {
        rankingPages = pages;




    }

    
    private void organizeFetcherThreads() {
        for (int i = 0; i < rankingPages.length; i++) {
            URL url = null;
            try {
                url = new URL (rankingPages[i].getUrl());
                URLConnection urlConnection = url.openConnection();
                int contentLength = urlConnection.getContentLength();

                // Clasificar las paginas segu tamao usando xxxxLimitPageContent (Constantes)
                // Llenar los arreglos Grupo 1...3

            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
    }
    

    private void startFetcherThreads() {

        // Inicio los 50 threads

        // Si group1 esta listo
        //      => crear indice
        //      => crear grafo

        // Si group2 esta listo
        //      => actualizar indice
        //      => actualizar grafo
        // Si group3 esta listo => actualizar index
        //      => actualizar indice
        //      => actualizar grafo
        
    }
    
    
    
}
