package core;

import java.io.IOException;

import org.apache.log4j.Logger;
import util.DBManager;
import util.IndexManager;
import util.PageRetrieval;
import web_services.BingSearchAPI;
import web_services.GoogleSearchAPI;
import web_services.Provider;
import web_services.WebSearcher;
import models.Page;
import models.Query;

/**
 * This class execute three main tasks:
 * a) Send a query to a search engine provider
 * b) Download the search results (pages)
 * c) Index the downladed pages from the search results
 *
 * @author Patricio Galeas
 * @version 1.1
 * @since 1.0
 */
public class Search {



    private static final Logger logger = Logger.getLogger(Search.class);


    /**
     * Metodo que realiza la busqueda, indexa y retorna el resultado de la busqueda
     */
    public static void initSearch(final Query query, Provider provider)  {

        logger.debug("Buscando por: " + query.getQuery());

        WebSearcher searchEngine;

        /**
         * Select the search engine providers.
         */
        if (provider == Provider.BING)
            searchEngine = new BingSearchAPI();
        else if (provider == Provider.GOOGLE)
            searchEngine = new GoogleSearchAPI();
        else
            searchEngine = new GoogleSearchAPI();



        /**
         * Download and index the pages retrieved from the search engine
         * in different threads
         */
        try {

            logger.info("Buscando páginas");

            /**
             * Execute the query in the search engine.
             */
            final Page[] pages = searchEngine.getPages(query, Constants.PAGES / 10);


            logger.info("Se descargarán los contenidos de "+pages.length+" páginas");


            /** Define one thread for each page download  */
            Thread[] threads = new Thread[pages.length];

            /**  Starts the Indexing Manager (Lucene)*/
            //final IndexManager idxManager = new IndexManager();
            //idxManager.openIndex();

            final DBManager db = new DBManager ();



            /**
             * Start the retrieve/indexing proccesses in different threads
             * */
            for (int i = 0; i < pages.length; i++) {

                final int ranking = i;
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //logger.info("Recuperando página  "+pages[ranking]);

                        /**
                         * retrieve the page content
                         * */
                        //PageRetrieval.retrieveContentPage(pages[ranking], true);
                        PageRetrieval.retrieveContentPage0(pages[ranking]);

                        /**
                         * Index the retrieved page
                         * */
                        //idxManager.indexPage(pages[ranking], ranking);

                        db.addPage (pages[ranking],ranking);


                    }
                });
                threads[i].start();
            }


            /**
             * Wait until all indexing threads are completed
             * and then close the indexing process  */
            for(int j = 0; j < threads.length; j++)
                threads[j].join();

            //idxManager.closeIndex();

            logger.info("Todos los download threads fueron cerrados exitosamente");

        } catch (IOException e) {
            logger.error("Error I/O en initSearch");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error("Error Threads en initSearch");
            e.printStackTrace();
        }


    }
}
