package com.sap.sea.worker.keeper;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sap.sea.util.HttpBot;
import com.sap.sea.util.HttpResult;
import com.sap.sea.util.Json;
import com.spotify.docker.client.messages.Image;

public class DefaultKeeper implements Keeper{
	private String hubIp;

	public DefaultKeeper(String hub) {
		hubIp = hub;
	}

	@Override
	public List<Image> images() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> search(String query) {
		List<String> list = new LinkedList<String>();
		JsonArray resultArray = searchAsJsonArray(query);
		for (JsonElement jsonElement : resultArray) {
			String image = jsonElement.getAsJsonObject().get("name").getAsString();
			list.add(image.replace("library", hubIp));
		}
		return list;
	}
	
	public JsonArray searchAsJsonArray(String query){
		HttpResult result = HttpBot.get("http://"+hubIp+"/v1/search?q="+query, null, null);
		String rs =result.getBody();
		JsonElement element = Json.parse(rs);
		return element.getAsJsonObject().get("results").getAsJsonArray();
	}
	
}
