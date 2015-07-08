package web_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Page;
import models.Query;


class BingSearchResults {

	public ResultsContent d;

	public static class ResultsContent {
	    public Result[] results;
	    public String __next;
	}

	public static class Result {
	    public String ID;
	    public String Title;
	    public String Description;
	    public String DisplayUrl;
	    public String Url;
	    public Metadata __metadata;
	
	}

	public static class Metadata {
	    public String uri;
	    public String type;
	}

}


public class BingSearchAPI implements WebSearcher{

	@Override
	public Page[] getPages(Query query, int size) {
		return SearchWithBing(query.getQuery());
	}
	
	
	private Page[] SearchWithBing(String search){

        search = search.replaceAll(" ", "%20");
        String accountKey="FKCnseSiVMVut/uO7otnQ20cIbhbEvlzCndml7lfSVw";
        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        URL url;
        try {
            url = new URL("https://api.datamarket.azure.com/Bing/Search/v1/Web?Query=%27" + search + "%27&$top=50&$format=json");

	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
	        conn.setRequestProperty("Accept", "application/json");
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));
	        String output = "";
	        String temp = "";
	        while ((temp = br.readLine()) != null) output += temp;
	        Gson json = (new GsonBuilder()).create();
	        BingSearchResults results = json.fromJson(output, BingSearchResults.class);
	        Page[] pages = new Page[results.d.results.length];
	        for (int i=0; i < results.d.results.length; i++)
	        {
	        	pages[i] = new Page(results.d.results[i].Url, results.d.results[i].Title, results.d.results[i].Description);
	        }
	        conn.disconnect();
	        return pages;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }
	
}

