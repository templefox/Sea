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
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

public class Docker {
	private Island island;

	public Docker(Island island) {
		this.island = island;
	}

	@GET
	@Path("{any: .*}")
	public Response getRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirectProxy(any,HttpMethod.GET,null);
	}

	@POST
	@Path("{any: .*}")
	public Response postRedirect(String entity,@PathParam("any") String any) throws URISyntaxException {
		return redirectProxy(any, HttpMethod.POST, Entity.text(entity));
	}

	@PUT
	@Path("{any: .*}")
	public Response putRedirect(String entity,@PathParam("any") String any) throws URISyntaxException {
		return redirectProxy(any, HttpMethod.PUT, Entity.text(entity));
	}

	@DELETE
	@Path("{any: .*}")
	public Response deleteRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirectProxy(any,HttpMethod.DELETE,null);
	}

	private Response redirectProxy(String any,String method,Entity<?> entity) throws URISyntaxException {
		try {
			JerseyClient client = JerseyClientBuilder.createClient();
			JerseyWebTarget webTarget = client.target(new URI("http://" + island.getIp() + "/" + any));
			Response response = webTarget.request().async().method(method,entity).get();
			return response;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
}
