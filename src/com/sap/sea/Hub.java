package com.sap.sea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sap.sea.util.HttpBot;
import com.sap.sea.util.HttpResult;
import com.sap.sea.util.Json;

@Path("/hub")
@Singleton
public class Hub {
	@Context
	ServletContext context;
	
	private String hubip;
	
	@PUT
	@Path("/ip")
	public Response setIp(String ip){
		hubip = ip;
		return Response.ok().build();
	}
	
	@GET
	@Path("/ip")
	public Response getIp(){
		return Response.ok(hubip).build();
	}
	
	@GET
	@Path("/images")
	@Produces({ "text/plain", "application/json" })
	public String searchImages(@DefaultValue("library") @QueryParam("q") String query,
			@DefaultValue("json") @QueryParam("format") String format) throws FileNotFoundException, IOException {
		

		HttpResult result = HttpBot.get("http://" + hubip + "/v1/search?q=" + query, null, null);
		String rs = result.getBody();
		JsonElement element = Json.parse(rs);
		JsonArray array = element.getAsJsonObject().get("results").getAsJsonArray();
		return array.toString();
	}
	
	@GET
	@Path("{any: v1/.*}")
	public Response getRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + hubip + "/" + any)).build();
	}

	@POST
	@Path("{any: v1/.*}")
	public Response postRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + hubip + "/" + any)).build();
	}

	@PUT
	@Path("{any: v1/.*}")
	public Response putRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + hubip + "/" + any)).build();
	}

	@DELETE
	@Path("{any: v1/.*}")
	public Response deleteRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + hubip + "/" + any)).build();
	}
}
