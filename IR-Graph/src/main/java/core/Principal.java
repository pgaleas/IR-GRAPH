package core;

import models.Query;
import web_services.Provider;


public class Principal 
{
	
	/**
	 * M&eacute;todo principal, dispara procedimiento de búsqueda e indexación.
	 * 
	 * @param args
	 */
	public static void main(String args[])  {

		long init = System.currentTimeMillis();

		Query q = new Query("patricio galeas");
		
		Search.initSearch(q, Provider.BING);
	}

}