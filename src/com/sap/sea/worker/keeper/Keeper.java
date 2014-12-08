package com.sap.sea.worker.keeper;

import java.util.List;

import com.google.gson.JsonArray;
import com.spotify.docker.client.messages.Image;

public interface Keeper {
	List<Image> images();

	List<String> search(String string);
	
	JsonArray searchAsJsonArray(String query);
}
