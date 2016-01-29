package web_services;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import models.Page;
import models.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GoogleSearchAPI implements WebSearcher {

	/*
	private String key = "AIzaSyCV7k5XHuO5hEy1y6YjUVHvP2_mMLqtjHE";
	private String cx = "008965614622624884785:ialisl9b7h4";
	*/
	
	
	private String key = "AIzaSyCslK1FCnCjeAdKHwcVOYCab1om82_7IfE";
	private String cx = "008965614622624884785:a-y2w5vzeko";
	
	/*
	private String key = "AIzaSyC1oAy9PdGGdREeQ2KOMj6CPaj0AhB3U3E";
	private String cx = "008965614622624884785:x60dqoortxm";
	*/
	
	private String url = "https://www.googleapis.com/customsearch/v1";
	private Scanner s;
	
	
	public GoogleSearchAPI() {
		
	}
	
	@Override
	public Page[] getPages(Query query, int size) throws IOException {
		query.getQuery();
		Page[][] pages = new Page[size][10];
		Page[] result = new Page[size*10];
		String u = url+"?cx="+cx+"&key="+key+"&q="+query;
		for (int i=1; i < size*10; i+=10)
		{
			pages[(i-1)/10] = getPagesFromJson(getJsonFromUrl(u+"&start="+i));
		}
		
		for (int i=0; i<size; i++)
		{
			for (int j=0; j < 10; j++)
			{
				result[i*10+j] = pages[i][j];
			}
		}
		
		return result;
	}
	
	
	private Page[] getPagesFromJson(String json)
	{
		Page[] pages = new Page[10];
		JSONObject rootObject = (JSONObject) JSONValue.parse(json);
		JSONArray items = (JSONArray) rootObject.get("items");
		
		for (int i=0; i < 10; i++)
		{
			JSONObject t = (JSONObject) items.get(i);
			pages[i] = new Page((String)t.get("link"), (String)t.get("title"), (String)t.get("snippet"));
		}
		return pages;
	}
	
	
	private String getJsonFromUrl(String url) throws IOException
	{
		URL jsonUrl = new URL(url);
		String json = "";
		s = new Scanner(jsonUrl.openStream());
		while (s.hasNext()) {
			json += s.nextLine();
		}
		return json;
	}
	
}
