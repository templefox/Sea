package com.sap.sea;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sea.selector.RandomSelector;
import com.sap.sea.selector.Selector;

@Path("/")
@Singleton
public class Sea {
	final private TreeMap<String, Island> islands = new TreeMap<String, Island>();
	private static Jedis jedis;
	private static String jhost;
	private static String jport;
	private ObjectMapper mapper = new ObjectMapper();

	@Context
	public void initialize(ServletContext context) throws Exception {
		jhost = context.getInitParameter("dao.host");
		jport = context.getInitParameter("dao.port");
		try {

			connectJedis();

			readIslandsFromRedis();
		} catch (JedisConnectionException e) {
			throw new Exception("Jedis connect error", e);
		}
	}

	private void readIslandsFromRedis() {
		try {
			Set<String> loadedIslands = getJedis().smembers("ISLANDS");
			for (String string : loadedIslands) {
				Island island = mapper.readValue(string, Island.class);
				if (island.check()) {
					islands.put(island.getIp(), island);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Path("/selector/{name}")
	public Selector navigator(@PathParam("name") String name) {
		String realName = "com.sap.sea.selector." + Character.toUpperCase(name.charAt(0)) + name.substring(1)
				+ "Selector";
		try {
			Class<? extends Selector> selector = (Class<? extends Selector>) Class.forName(realName);
			Constructor<? extends Selector> constructor = (Constructor<Selector>) selector
					.getConstructor(TreeMap.class);
			return constructor.newInstance(islands);

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Path("/island/{ip:[0-9:\\.]*}")
	public Island getIsland(@PathParam("ip") String ip) {
		Island island = islands.get(ip);
		island.enableShell(true);
		return island;
	}

	@GET
	@Path("/islands/list")
	public Response listIslands() {
		try {
			return Response.ok(mapper.writeValueAsString(islands.keySet())).build();
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
				getJedis().sadd("ISLANDS", mapper.writeValueAsString(island));
				islands.put(island.getIp(), island);
				return Response.ok().build();
			} else {
				return Response.serverError().entity("Wrong Format").build();
			}
		} catch (JsonProcessingException e) {
			return returnException(e);
		}
	}

	@DELETE
	@Path("/islands/list/{ip}")
	public Response removeIsland(@PathParam("ip") String ip) {
		try {
			Island island = islands.remove(ip);
			if (island != null) {
				getJedis().srem("ISLANDS", mapper.writeValueAsString(island));
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

	private Response returnException(Exception e) {
		e.printStackTrace();
		return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
	}

	public static Jedis getJedis() {
		try {
			if (!jedis.ping().equals("PONE")) {
				connectJedis();
			}			
		} catch (Exception e) {
			connectJedis();
		}
		return jedis;
	}

	private static void connectJedis() {
		Jedis jedis = new Jedis(jhost, Integer.valueOf(jport));
		jedis.select(1);
		setJedis(jedis);
	}

	private static void setJedis(Jedis jedis) {
		Sea.jedis = jedis;
	}
}
