package web_services;

import java.io.IOException;

import models.*;

/**
 * 
 * Interfaz para el manejo de los diferentes proovedores de servicios de busqueda.
 * 
 * @author Javier Fuentes (j.fuentes06@ufromail.cl)
 * @version 1.0
 * @since 1.0
 *
 */
public interface WebSearcher {

	/**
	 * 
	 * Permite obtener las paginas, sin contenido, desde el buscador.   
	 * @param query
	 * @param size
	 * @throws IOException
	 * @return lista con las paginas obtenidas.
	 */
	public Page[] getPages(Query query, int size) throws IOException;

}
