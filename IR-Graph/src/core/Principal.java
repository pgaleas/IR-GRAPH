package core;


import models.Query;
import util.DBManager;
import web_services.Provider;



/**
 *	Clase disparadora.
 * 
 * @author      Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version     1.0
 * @since       1.0
 */
public class Principal 
{
	
	/**
	 * M&eacute;todo principal, dispara procedimiento de b&uacute;squeda e indexaci&oacute;n.
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		long init = System.currentTimeMillis();
		
		
		
		Query q = new Query("energia eolica nuclear solar");
		
		Search.initSearch(q, Provider.BING);
		
		/*
		try{
			new Test();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
		
		System.out.println("-------------------");
		System.out.print("Tiempo (seg): ");
		System.out.println((System.currentTimeMillis() - init)/1000.0F);
	}

}