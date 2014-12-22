package com.sap.sea;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import redis.clients.jedis.Jedis;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Path("/")
@Singleton
public class Sea {
	final private TreeMap<String, Island> islands = new TreeMap<String, Island>();
	private static Jedis jedis;

	private ObjectMapper mapper = new ObjectMapper();

	@Context
	public void initialize(ServletContext context) {
		String host = context.getInitParameter("dao.host");
		String port = context.getInitParameter("dao.port");
		setJedis(new Jedis(host, Integer.valueOf(port)));

		readIslandsFromRedis();
	}

	private void readIslandsFromRedis() {
		try {
			Set<String> loadedIslands = jedis.smembers("ISLANDS");
			for (String string : loadedIslands) {
				System.out.println(string);
				Island island = mapper.readValue(string, Island.class);
				islands.put(island.getIp(), island);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		try {
			return Response.ok(mapper.writeValueAsString(islands)).build();
		} catch (JsonProcessingException e) {
			return returnException(e);
		}
	}

	@POST
	@Path("/islands/list")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response putIsland(Island island) {
		try {
			if (island.check()) {
				jedis.sadd("ISLANDS", mapper.writeValueAsString(island));
				islands.put(island.getIp(), island);
				return Response.ok().build();
			} else {
				return Response.serverError().entity("Wrong Format").build();
			}
		} catch (JsonProcessingException e) {
			return returnException(e);
		}
	}

	private Response returnException(JsonProcessingException e) {
		e.printStackTrace();
		return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
	}

	@DELETE
	@Path("/islands/list/{ip}")
	public Response removeIsland(@PathParam("ip") String ip) {
		try {
			Island island = islands.remove(ip);
			if (island != null) {
				jedis.srem("ISLANDS", mapper.writeValueAsString(island));
				return Response.ok().build();
			} else {
				return Response.serverError().entity("Wrong Format").build();
			}
		} catch (JsonProcessingException e) {
			return returnException(e);
		}
	}

	@POST
	@Path("/test")
	public Response test() throws URISyntaxException {

		Response response = Response.temporaryRedirect(new URI("http://10.59.144.147:8080/Sea/test2/adfsa/adsf/adsf"))
				.build();
		return response;
	}

	@POST
	@Path("/test2/{ip: .*}")
	public Response test2(String content, @PathParam("ip") String ip) throws URISyntaxException {
		return Response.ok(ip + content).build();
	}

	@Path("/hub")
	public Class<Hub> getHub() {
		return Hub.class;
	}

	public static Jedis getJedis() {
		return jedis;
	}

	private static void setJedis(Jedis jedis) {
		Sea.jedis = jedis;
	}
}
