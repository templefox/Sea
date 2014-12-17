package com.sap.sea;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Path("/")
@Singleton
public class Sea {
	final private TreeMap<String, Island> islands = new TreeMap<String, Island>();

	public Sea() {
		Island island = new Island();
		island.setIp("10.58.136.164:4567");
		islands.put(island.getIp(), island);
	}

	@Path("/navigator/")
	public Island get() {
		for (String key : islands.keySet()) {
			Island island = islands.get(key);
			if (island.available()) {
				return island;
			}
		}
		return null;
	}

	@Path("/island/{ip}")
	public Island getIsland(@PathParam("ip") String ip) {
		Island island = islands.get(ip);
		return island;
	}

	@GET
	@Path("/islands/list")
	public Response listIslands() {
		JsonElement element = new Gson().toJsonTree(islands.keySet(), new TypeToken<Set<String>>() {
		}.getType());
		return Response.ok(element.toString()).build();
	}

	@PUT
	@Path("/islands/list")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response putIsland(Island island) {
		if (island.check()) {
			islands.put(island.getIp(), island);
			return Response.ok().build();
		} else {
			return Response.serverError().entity("Wrong Format").build();
		}
	}

	@DELETE
	@Path("/islands/list/{ip}")
	public Response removeIsland(@PathParam("ip") String ip) {
		Island island = islands.remove(ip);
		if (island != null) {
			return Response.ok().build();
		} else {
			return Response.serverError().entity("Wrong Format").build();
		}
	}

	@POST
	@Path("/test")
	public Response test() throws URISyntaxException {
		
		Response response = Response.temporaryRedirect(new URI("http://10.59.144.147:8080/Sea/test2/adfsa/adsf/adsf")).build();
		return response;
	}

	@POST
	@Path("/test2/{ip: .*}")
	public Response test2(String content,@PathParam("ip") String ip) throws URISyntaxException {
		return Response.ok(ip+content).build();
		// return Response.ok("aaa").build();
	}
}
