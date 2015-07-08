package util;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import models.Page;
import models.Query;
import core.Constants;


/**
 *	Clase con utilidades para el manejo de los datos. 
 * 
 * @author Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version 1.0
 * @since 1.0
 * 
 */
public class DBManager {
	/**
	 * Parametros de configuracion de la base de datos.
	 */
	private static Scanner s;
	
	/**
	 * Permite revisar si la query ya fue buscada previamente.
	 * 
	 * @param q
	 * @return boolean true en caso de encontrar una consulta, false en caso contrario.
	 */
	public static boolean isQueryInCache(Query q)
	{
		File f= new File(Constants.BASE_URL+"queries/"+toSHA2(q.getQuery()));
		return f.exists();
	}
	
	/**
	 * Permite revisar si la pagina ya fue descargada.
	 * 
	 * @param page
	 * @param q
	 * @return boolean true en caso de encontrar la pagina, false en caso contrario.
	 * 
	 */
	public static boolean isPageInCache(Page page, Query q)
	{
		File f = new File(Constants.BASE_URL + "pages/" + toSHA2(page.getUrl()));
		return f.exists();
	}
	/**
	 * Crea el directorio para las paginas descargadas para una query 
	 * 
	 * @param q
	 * @return Retorna @true en caso de que la query no exista, @false en caso contrario
	 */
	public static boolean addPageQueryIfNotExists(Query q)
	{
		File f = new File(Constants.BASE_URL +  "pages/" + toSHA2(q));
		if (!f.exists())
		{
			f.mkdirs();
			return true;
		}
		return false;
	}
	
	
	/**
	 * Elimina una query completa, incluyendo sus paginas y su indice relacionado.
	 * 
	 * @param q
	 */
	public static void removeQuery(Query q)
	{
		
		try {
			File f;
			
			/**
			 * Remover paginas.
			 */
			ArrayList<String> urls;
			urls = getUrls(q);
			for (String url : urls) {
				f = new File(Constants.BASE_URL + "pages/" + toSHA2(url));
				f.delete();
			}
			
			/**
			 * Remover queries
			 */
			f = new File(Constants.BASE_URL + "queries/" + toSHA2(q.getQuery()));
			f.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
	/**
	 * Ingresa una query a la base de datos, incluyendo las paginas relacionadas a la misma.
	 * @param q
	 * @param pages
	 */
	public static void addQuery(Query q, Page[] pages) 
	{
		try 
		{
			/*
			 * En caso de que el directorio no exista, se fuerza la creacion.
			 */
			File aux = new File(Constants.BASE_URL + "queries/");
			if (!aux.exists()) aux.mkdirs();
			
			File f= new File(Constants.BASE_URL+"queries/"+toSHA2(q.getQuery()));
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (int i=0; i < pages.length; i++) {
				bw.write(pages[i].getUrl());
				if (i+1 < pages.length) bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Permite ingresar una pagina al indice.
	 * 
	 * @param p
	 * @param q
	 */
	public static void addPage(Page p, Query q, int id)
	{
		File file = new File(Constants.BASE_URL + "pages/" + toSHA2(q) + "/" + id);
		if (file.exists())
		{
			throw new RuntimeException("Archivo ya existe");
		}
		else
		{
			try
			{
				FileWriter writer = new FileWriter(file);
				writer.write(p.getContent());
				writer.flush();
				writer.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Permite ingresar un snippet al indice.
	 * 
	 * @param p
	 * @param q
	 */
	public static void addPageSnippet(Page p, Query q, int id)
	{
		/*
		 * Fuerza la creacion del directorio
		 */
		File aux = new File(Constants.BASE_URL + "snippets/" + toSHA2(q));
		if (!aux.exists()) aux.mkdirs();
		
		File file = new File(Constants.BASE_URL + "snippets/" + toSHA2(q) + "/" + id);
		if (file.exists())
		{
			throw new RuntimeException("Archivo ya existe");
		}
		else
		{
			try
			{
				FileWriter writer = new FileWriter(file);
				writer.write(p.getSnippet());
				writer.flush();
				writer.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Permite ingresar un snippet al indice.
	 * 
	 * @param p
	 * @param q
	 */
	public static void addPageTitle(Page p, Query q, int id)
	{
		/*
		 * Fuerza la creacion del directorio
		 */
		File aux = new File(Constants.BASE_URL + "titles/" + toSHA2(q));
		if (!aux.exists()) aux.mkdirs();
		
		File file = new File(Constants.BASE_URL + "titles/" + toSHA2(q) + "/" + id);
		if (file.exists())
		{
			throw new RuntimeException("Archivo ya existe");
		}
		else
		{
			try
			{
				FileWriter writer = new FileWriter(file);
				writer.write(p.getTitle());
				writer.flush();
				writer.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Obtiene una lista con las paginas relacionadas a la query.
	 * @param q
	 * @return lista con paginas.
	 */
	public static Page[] getPagesFromQuery(Query q)
	{
		try {
			ArrayList<String> urls = getUrls(q);
			Page[] pages = new Page[urls.size()];
			for (int i=0; i < urls.size(); i++)
			{
				File f = new File(Constants.BASE_URL +  "pages/" + toSHA2(urls.get(i)));
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				pages[i] = (Page) ois.readObject();
				ois.close();
			}
			return pages;
		}
		catch(Exception e)
		{
			return null;
		}
		
		
	}
	
	
	/**
	 * Obtiene un arreglo con las urls de una query.
	 *  
	 * @param q
	 * @return array con las urls.
	 * @throws FileNotFoundException
	 */
	private static ArrayList<String> getUrls(Query q) throws FileNotFoundException
	{
		File f = new File(Constants.BASE_URL + "queries/" + toSHA2(q.getQuery()));
		s = new Scanner(f);
		ArrayList<String> urls = new ArrayList<>();
		while (s.hasNext()) urls.add(s.nextLine());
		return urls;
	}
	
	/**
	 * Genera un SHA-2 de una query, como funcion hash para almacenar indices.
	 * @param q
	 * @return String con el hash SHA-2
	 */
	public static String toSHA2(String q)
	{
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(q.getBytes());
			byte[] byteData = digest.digest();
			
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo sobrecargado
	 * 
	 * @param q
	 * @return String con el hash SHA-2
	 */
	public static String toSHA2(Query q)
	{
		return toSHA2(q.getQuery());
	}


}
