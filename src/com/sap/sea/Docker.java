package com.sap.sea;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class Docker {
	private Island island;
	public Docker(Island island) {
		this.island =island;
	}

	@GET
	@Path("{any: .*}")
	public Response getRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@POST
	@Path("{any: .*}")
	public Response postRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@PUT
	@Path("{any: .*}")
	public Response putRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	@DELETE
	@Path("{any: .*}")
	public Response deleteRedirect(@PathParam("any") String any) throws URISyntaxException {
		return redirect(any);
	}

	private Response redirect(String subPath) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + island.getIp() + "/" + subPath)).build();
	}
}
