package com.sap.sea.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sap.sea.util.HttpBot;
import com.sap.sea.util.HttpResult;
import com.sap.sea.util.Json;
import com.sap.sea.util.Props;

@Path("/storekeeper")
public class Storekeeper {
	@Context
	ServletContext context;

	@GET
	@Path("/images")
	@Produces({ "text/plain", "application/json" })
	public String searchImages(@DefaultValue("library") @QueryParam("q") String query,
			@DefaultValue("json") @QueryParam("format") String format) throws FileNotFoundException, IOException {
		Props.load(new FileInputStream(context.getRealPath("WEB-INF") + "/config.properties"));
		String hub = Props.instance().getProperty("hub");

		HttpResult result = HttpBot.get("http://" + hub + "/v1/search?q=" + query, null, null);
		String rs = result.getBody();
		JsonElement element = Json.parse(rs);
		JsonArray array = element.getAsJsonObject().get("results").getAsJsonArray();
		return array.toString();
	}
}
