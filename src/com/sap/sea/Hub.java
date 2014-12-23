package com.sap.sea;

import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import redis.clients.jedis.Jedis;

@Singleton
public class Hub {
	
	
	private static final String HUBIP = "hubip";
	private String hubip;
	private Jedis jedis;
	
	public Hub() {
		jedis = Sea.getJedis();
		hubip = jedis.get(HUBIP);
	}
	
	
	@POST
	@Path("/ip")
	public Response setIp(String ip){
		jedis.set(HUBIP, ip);
		hubip = ip;
		return Response.ok().build();
	}
	
	@GET
	@Path("/ip")
	public Response getIp(){
		return Response.ok(hubip).build();
	}
	
	@GET
	@Path("{any: v1/.*}")
	public Response getRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@POST
	@Path("{any: v1/.*}")
	public Response postRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@PUT
	@Path("{any: v1/.*}")
	public Response putRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@DELETE
	@Path("{any: v1/.*}")
	public Response deleteRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	private Response redirect(String subPath) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + hubip + "/" + subPath)).build();
	}
}
