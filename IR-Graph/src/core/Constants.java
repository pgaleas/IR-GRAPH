package core;

/**
 * Constantes para almacenamiento en el &iacute;ndice.
 * 
 * @author javier
 * @version 1.0
 * @since 1.0
 */
public class Constants {
	public static final String BASE_URL="/Users/javier/files/";
	   public static final String CONTENTS="contents";
	   public static final String URL="url";
	   public static final String TITLE="title";
	   public static final String SNIPPET="snippet"; 
	   public static final String DOCID="doc_id"; 
	   public static final int MAX_SEARCH = 10;
	   public static int PAGES = 50;
	   /**
		 * Aumenta el tamaño del documento en un 10% al comienzo de este,
		 *  para evitar los efectos que produce la funcion de forier en 
		 *  los bordes del documento por la periodicidad de las funciones seno y coseno
		 */
	   public static final float PORCENTAJE_LARGO_EXTRA_DOCUMENTO = 0.15F;
}