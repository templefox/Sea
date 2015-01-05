package com.sap.sea;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class Docker {
	private Island island;

	public Docker(Island island) {
		this.island = island;
	}

	@GET
	@Path("{any: .*}")
	public Response getRedirect(@PathParam("any") String any, @Context UriInfo info) throws URISyntaxException {
		return redirectProxy(any, HttpMethod.GET, null, info);
	}

	@POST
	@Path("{any: .*}")
	public Response postRedirect(String entity, @PathParam("any") String any, @Context UriInfo info)
			throws URISyntaxException {
		return redirectProxy(any, HttpMethod.POST, Entity.json(entity), info);
	}

	@PUT
	@Path("{any: .*}")
	public Response putRedirect(String entity, @PathParam("any") String any, @Context UriInfo info)
			throws URISyntaxException {
		return redirectProxy(any, HttpMethod.PUT, Entity.json(entity), info);
	}

	@DELETE
	@Path("{any: .*}")
	public Response deleteRedirect(@PathParam("any") String any, @Context UriInfo info) throws URISyntaxException {
		return redirectProxy(any, HttpMethod.DELETE, null, info);
	}

	private Response redirectProxy(String any, String method, Entity<?> entity, UriInfo info) throws URISyntaxException {
		try {
			JerseyClient client = JerseyClientBuilder.createClient();
			JerseyWebTarget webTarget = client.target(new URI("http://" + island.getIp() + "/" + any));
			MultivaluedMap<String, String> map = info.getQueryParameters();
			for (String key : map.keySet()) {
				webTarget = webTarget.queryParam(key, map.get(key).toArray());
			}
			Response response = webTarget.request().async().method(method, entity).get();
			MultivaluedMap<String, Object> headers = response.getHeaders();
			headers.remove("Transfer-Encoding");
			return Response.fromResponse(response).replaceAll(headers).build();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
}
