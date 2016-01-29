package util;




import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import models.Page;
import core.Constants;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 *	Clase con utilidades para el manejo de los datos.
 * 
 * @author Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version 1.0
 * @since 1.0
 * 
 */
public class DBManager {



	private static final Logger logger = Logger.getLogger(DBManager.class);

	private Jedis jedisConnector;


	public DBManager() {
		jedisConnector = new Jedis(Constants.REDIS_URL);
	}

    /**
     * Permite ingresar una pagina al indice.
     */
    public void addPage(Page p, int ranking)
    {

        Map<String, String> urlProperties = new HashMap<String, String>();
        urlProperties.put("url", p.getUrl());
        urlProperties.put("title", p.getTitle());
        if(p.getContent() != null) urlProperties.put("content", p.getContent());
        urlProperties.put("snippet", p.getSnippet());


        if(p.getUrl() != null) {
            logger.info("Adding page content to redis database :" + p.getUrl());
			String s = jedisConnector.hmset("result:"+ranking, urlProperties);
			logger.info("Page ("+ranking+") content added to Redis database : " + s);
		} else
            logger.error("Error adding page content to Redis database :" + p.getUrl());
    }


	/**
	 * Permite revisar si la query ya fue buscada previamente.
	 *
	 * @param q
	 * @return boolean true en caso de encontrar una consulta, false en caso contrario.

	public static boolean isQueryInCache(Query q)
	{
		File f= new File(Constants.BASE_URL+"urls/"+toSHA2(q.getQuery()));
		return f.exists();
	}
	*/

	/**
	 * Permite revisar si la pagina ya fue descargada.
	 *
	 * @param page
	 * @param q
	 * @return boolean true en caso de encontrar la pagina, false en caso contrario.
	 *

	public static boolean isPageInCache(Page page, Query q)
	{
		File f = new File(Constants.BASE_URL + "pages/" + toSHA2(page.getUrl()));
		return f.exists();
	}
	*/

	/**
	 * Crea el directorio para las paginas descargadas para una query
	 *
	 * @param q
	 * @return Retorna @true en caso de que la query no exista, @false en caso contrario

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
	*/

	/**
	 * Elimina una query completa, incluyendo sus paginas y su indice relacionado.
	 *
	 * @param q

	public static void removeQuery(Query q)
	{

		try {
			File f;


			ArrayList<String> urls;
			urls = getUrls(q);
			for (String url : urls) {
				f = new File(Constants.BASE_URL + "pages/" + toSHA2(url));
				f.delete();
			}


			f = new File(Constants.BASE_URL + "urls/" + toSHA2(q.getQuery()));
			f.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		*/



	/**
	 * Ingresa una query a la base de datos, incluyendo las paginas relacionadas a la misma.

	public static void addQuery(Query q, Page[] pages)
	{
		try
		{

			 // En caso de que el directorio no exista, se fuerza la creacion.

			File aux = new File(Constants.BASE_URL + "urls/");
			if (!aux.exists()) aux.mkdirs();

			File f= new File(Constants.BASE_URL+"urls/"+toSHA2(q.getQuery()));
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
	*/
	




	/**
	 * Obtiene una lista con las paginas relacionadas a la query.
	 * @param q
	 * @return lista con paginas.

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
	*/


	/**
	 * Obtiene un arreglo con las urls de una query.
	 *
	 * @param q
	 * @return array con las urls.
	 * @throws FileNotFoundException

	private static ArrayList<String> getUrls(Query q) throws FileNotFoundException
	{
		File f = new File(Constants.BASE_URL + "urls/" + toSHA2(q.getQuery()));
		s = new Scanner(f);
		ArrayList<String> urls = new ArrayList();
		while (s.hasNext()) urls.add(s.nextLine());
		return urls;
	}
	*/



	/**
	 * Genera un SHA-2 de una query, como funcion hash para almacenar indices.
	 * @param q
	 * @return String con el hash SHA-2
	 */
	/*
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
	} */

	/**
	 * Metodo sobrecargado
	 *
	 * @param q
	 * @return String con el hash SHA-2
	 */
	/*
	public static String toSHA2(Query q)
	{
		return toSHA2(q.getQuery());
	}*/


}
