package util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import java.io.IOException;


/**
 * Created by patricio on 23-12-15.
 */
public class SearchManager {

    private IndexSearcher searcher;
    private Query query;
    QueryParser parser;
    private int hitsPerPage;

    private SearchManager() {

    }

    public void initializeSearch(Directory dir, int nHits) {
        IndexReader reader;
        try {
            hitsPerPage = nHits;

            reader = DirectoryReader.open(dir);

            searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();

            parser = new QueryParser("content", analyzer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void doSearch(String queryString) {

        // Collect enough docs to show 5 pages
        TopDocs results;
        try {


            query = parser.parse(queryString);

            results = searcher.search(query, 5 * hitsPerPage);
            ScoreDoc[] hits = results.scoreDocs;

            int numTotalHits = results.totalHits;

            System.out.println(numTotalHits + " total matching documents");


            for (ScoreDoc hit : hits) {

                Document doc = searcher.doc(hit.doc);
                String ranking = doc.get("ranking");
                String url = doc.get("url");
                String title = doc.get("title");
                String snippet = doc.get("snippet");
                //String contents = doc.get("contents");

                System.out.println(ranking + ". " + url);
                System.out.println(title);
                System.out.println(snippet + "\n");

            }


            } catch (IOException | ParseException e1) {
            e1.printStackTrace();
        }
    }

}
