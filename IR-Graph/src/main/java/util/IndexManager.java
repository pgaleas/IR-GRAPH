package util;

import models.Page;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import java.io.IOException;


public class IndexManager {

    private static final Logger logger = Logger.getLogger(IndexManager.class);
    private IndexWriter writer;

    public IndexManager() {

    }

    /**
     * Open the index
     * */
    public void openIndex() {
        try {

            logger.info("Inicio de indexado en RAM ...");

            Directory dir = new RAMDirectory();

            Analyzer analyzer = new StandardAnalyzer();

            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            writer = new IndexWriter(dir, iwc);


        } catch (IOException e) {
            logger.error("El indice en RAM no se pudo abrir");
        }
    }


    /**
     * Close the lucene Index
     * */
    public void closeIndex() {
        try {
            logger.info("Cerrando indexado en RAM ...");
            writer.close();
        } catch (IOException e) {
            logger.error("El indice en RAM no se pudo cerrar");
            e.printStackTrace();
        }
    }


    /**
     * Indexes a single page
     */
    public void indexPage(Page page, int ranking) {

        // Make a new, empty document
        Document doc = new Document();

        doc.add(new IntField("ranking", ranking, Field.Store.YES));
        doc.add(new TextField("url", page.getUrl(), Field.Store.YES));
        doc.add(new TextField("title", page.getTitle(), Field.Store.YES));
        doc.add(new TextField("snippet", page.getSnippet(), Field.Store.YES));
        doc.add(new TextField("contents", page.getContent(), Field.Store.YES));

        System.out.println("adding " + page.getUrl());
        try {
            writer.addDocument(doc);
            logger.debug("La página " + page.getUrl() + " fue indexada");
        } catch (IOException e) {
            logger.error("La página " + page.getUrl() + " no pudo ser indexada");
        }
    }
}
