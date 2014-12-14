package com.sap.sea;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spotify.docker.client.messages.Image;

@Path("/")
@Singleton
public class Sea {
	final private TreeMap<String, Island> islands = new TreeMap<String,Island>();

	public Sea() {
		islands.put("a", new Island());
		islands.put("b", new Island());
	}
	
	@Path("/navigator/")
	public Test get(){
		return new Test();
	}
	
	@GET
	@Path("/islands/list")
	public Response listIslands(){
		JsonElement element = new Gson().toJsonTree(islands.keySet(), new TypeToken<Set<String>>() {
		}.getType());
		return Response.ok(element.toString()).build();
	}
	
	@PUT
	@Path("/islands")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIsland(Island island){
		
		
		return Response.ok("ok").build();
	}
	
	@GET
	@Path("/island")
	@Produces(MediaType.APPLICATION_JSON)
	public Island utIsland(){
		Island lIsland = new Island();
		lIsland.setIp("1");lIsland.setPass("2");lIsland.setUser("3");
		return lIsland;
	}
}
