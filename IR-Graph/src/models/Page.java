package models;

import java.io.Serializable;


/**
 * 
 * Clase que maneja la estructura de las p&aacute;ginas almacenadas en el cache.
 * 
 * @author javier
 * @version 1.0
 * @since 1.0
 */
public class Page implements Serializable{

	/**
	 * 
	 * Clase que maneja el modelo de datos para una p&aacute;gina, 
	 * 
	 * @author Javier Fuentes (j.fuentes06@ufromail.cl)
	 * @version 1.0
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String url;
	private String title;
	private String snippet;
	private String content;
	
	/**
	 * Obtiene la Url de la p&aacute;gina.
	 * 
	 * @return Url de la p&aacute;gina
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Asigna la Url a la p&acute;gina.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Obtiene el t&iacute;tulo de la p&acute;gina.
	 * @return t&iacute;tulo de la p&aacute;gina.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Obtiene el t&iacute;tulo de la p&aacute;gina.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Obtiene el snippet de la p&aacute;gina.
	 * @return snippet
	 */
	public String getSnippet() {
		return snippet;
	}
	
	/**
	 * Asgina un snippet a la p&aacute;gina.
	 * @param snippet
	 */
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	/**
	 * Obtiene el contenido plano de la p&aacute;gina.
	 * @return Contenido
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Asigna el contenido limpio a la p&aacute;gina.
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	

	/**
	 * Constructor de la clase
	 * 
	 * <p> Inicialmente los valores asignados son los obtenidos desde el proveedor de b&uacute;squeda,
	 * luego son descargados y asignados. 
	 * 
	 * @see util.PageRetrieval
	 * @param url
	 * @param title
	 * @param snippet
	 */
	public Page(String url, String title, String snippet) {
		super();
		this.url = url;
		this.title = title;
		this.snippet = snippet;
	}
	
	@Override
	public String toString() {
		return "Page [url=" + url + ", title=" + title + ", snippet=" + snippet+ "]";
	}

}