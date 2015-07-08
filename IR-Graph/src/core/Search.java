package core;

import java.io.IOException;


import util.DBManager;
import util.PageRetrieval;
import web_services.BingSearchAPI;
import web_services.GoogleSearchAPI;
import web_services.Provider;
import web_services.WebSearcher;
import models.Page;
import models.Query;

/**
 * 
 * Clase principal para el proceso completo de b&uacute;squeda.
 * 
 * @author      Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version 1.0
 * @since 1.0
 */
public class Search {
	
	/**
	 * Cantidad de p&aacute;ginas a buscar.
	 */
	
	
	/**
	 * M&eacute;todo que realiza la b&uacute;squeda, indexa y retorna el resultado de la b&uacute;squeda 
	 * en base a los parametros de distancia, {@link ResultRank}
	 * @param query consulta a ejecutar.
	 * @param provider proveedor de servicios de internet.
	 */
	public static void initSearch(Query query, Provider provider)
	{
		System.out.println("Buscando por: " + query.getQuery()+", SHA-2: "+DBManager.toSHA2(query));
		
		WebSearcher searchEngine;
		
		/**
		 * Seleccion del proveedor de busquedas. Solo disponible google y bing
		 */
		if (provider == Provider.BING) searchEngine = new BingSearchAPI();
		else if (provider == Provider.GOOGLE) searchEngine = new GoogleSearchAPI();
		else searchEngine = new GoogleSearchAPI();
		
		/**
		 * Si la query no se encuentra en la DB, se descargara.
		 */
		if (!DBManager.isQueryInCache(query))
		{
			try {
				
				System.out.println("Buscando paginas");
				
				/**
				 * Se buscan las paginas desde el proveedor de busquedas. 
				 */
				Page[] pages = searchEngine.getPages(query, Constants.PAGES/10);
				System.out.println("Paginas encontradas");
				
				
				
				/**
				 * Si la query ya fue descargada, se detiene el proceso.
				 */
				if (!DBManager.addPageQueryIfNotExists(query))
				{
					System.out.println("La consulta ya existe...");
					return;
				}
				
				
				/**
				 * Se descargan los contenidos, y se limpian (raw content)
				 * El proceso utiliza threads.
				 */
				Thread[] threads = new Thread[pages.length];
				for (int i=0; i < pages.length; i++)
				{
					int temp = i;
					threads[i] = new Thread(new Runnable() {
						@Override
						public void run() {
							System.out.println("Descargando contenidos crudos... ("+temp+")");
							PageRetrieval.retrieveContentPage(pages[temp]);
							System.out.println(pages[temp] + " - "+ temp);
							DBManager.addPage(pages[temp], query, temp);
							DBManager.addPageSnippet(pages[temp], query, temp);
							DBManager.addPageTitle(pages[temp], query, temp);
						}
					});
					threads[i].start();
				}
				
				/**
				 * Es necesario esperar que todos los threads abiertos terminen de realizar las descargas.
				 */
				for (int i=0; i < threads.length; i++) {
					try {
						while (threads[i].isAlive()) Thread.sleep(10);
						System.out.println("Thread: " + i + " terminado");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				/**
				 * La query se asocia a la base de datos.
				 */
				DBManager.addQuery(query, pages);
				System.out.println("Adding queries");
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
