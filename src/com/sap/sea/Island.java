package com.sap.sea;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;

public class Island {
	String ip;
	String user;
	String pass;

	@GET
	@Path("{any: .*}")
	public Response getRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + ip + "/" + any)).build();
	}

	@POST
	@Path("{any: .*}")
	public Response postRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + ip + "/" + any)).build();
	}

	@PUT
	@Path("{any: .*}")
	public Response putRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + ip + "/" + any)).build();
	}

	@DELETE
	@Path("{any: .*}")
	public Response deleteRedirect(@PathParam("any") String any) throws URISyntaxException {
		return Response.temporaryRedirect(new URI("http://" + ip + "/" + any)).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("/images")
	 * 
	 * @Produces("text/plain") public Response images() { DockerClient
	 * dockerClient = new DefaultDockerClient("http://" + ip); try { List<Image>
	 * images = dockerClient.listImages(); for (Image image : images) {
	 * ImmutableList<String> tags = image.repoTags(); for (String tag : tags) {
	 * System.out.println(tag); } } JsonElement element = new
	 * Gson().toJsonTree(images, new TypeToken<List<Image>>() { }.getType());
	 * return Response.ok(element.toString()).build(); } catch (DockerException
	 * | InterruptedException e) { e.printStackTrace(); return
	 * Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build(); }
	 * finally { dockerClient.close(); } }
	 * 
	 * @GET
	 * 
	 * @Path("/containers")
	 * 
	 * @Produces("text/plain") public String containers(@DefaultValue("false")
	 * @QueryParam("all") String all) { if (!Strings.isNullOrEmpty(ip)) {
	 * DockerClient dockerClient = new DefaultDockerClient("http://" + ip); try
	 * { ListContainersParam param = new ListContainersParam("all", all);
	 * List<Container> containers = dockerClient.listContainers(param); for
	 * (Container container : containers) { List<String> names =
	 * container.names(); for (String name : names) { System.out.println(name);
	 * } ; } JsonElement element = new Gson().toJsonTree(containers, new
	 * TypeToken<List<Container>>() { }.getType()); return element.toString(); }
	 * catch (DockerException | InterruptedException e) { e.printStackTrace(); }
	 * finally { dockerClient.close(); } } return "error"; }
	 * 
	 * 
	 * @GET
	 * 
	 * @Path("/start/{containerID}") public String run(@PathParam("ip") String
	 * ip, @PathParam("tag") String containerID) { if
	 * (!Strings.isNullOrEmpty(ip) && !Strings.isNullOrEmpty("tag")) { try {
	 * DockerClient dockerClient = new DefaultDockerClient("http://" + ip);
	 * Map<String, List<PortBinding>> portbinding = new HashMap<String,
	 * List<PortBinding>>(2); PortBinding binding = PortBinding.of("0.0.0.0",
	 * "30015"); List<PortBinding> list = new ArrayList<PortBinding>(1);
	 * list.add(binding); portbinding.put("30015/tcp", list); HostConfig config
	 * =
	 * HostConfig.builder().privileged(true).portBindings(portbinding).build();
	 * dockerClient.startContainer(containerID, config); return "success"; }
	 * catch (DockerException e) { e.printStackTrace(); } catch
	 * (InterruptedException e) { e.printStackTrace(); } } return "failed"; }
	 * 
	 * 
	 * @POST
	 * 
	 * @Path("/containers/start/{containerID}")
	 * 
	 * @Consumes({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON }) public
	 * Response startContainer(String jsonContent, @PathParam("containerID")
	 * String containerID) { DockerClient dockerClient = new
	 * DefaultDockerClient("http://" + ip); Throwable exception = null; try {
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); mapper.registerModule(new
	 * GuavaModule()); HostConfig config = mapper.readValue(jsonContent,
	 * HostConfig.class);
	 * 
	 * dockerClient.startContainer(containerID, config); return
	 * Response.ok("ok").build(); } catch (DockerException |
	 * InterruptedException | IOException e) { exception = e;
	 * e.printStackTrace(); } finally { dockerClient.close(); } return
	 * Response.serverError
	 * ().entity(ExceptionUtils.getStackTrace(exception)).build(); }
	 * 
	 * @DELETE
	 * 
	 * @Path("/containers/{containerID}") public Response
	 * stopContainer(@PathParam("containerID") String containerID,
	 * 
	 * @DefaultValue("0") @QueryParam("wait") Integer seconds) { DockerClient
	 * dockerClient = new DefaultDockerClient("http://" + ip); Throwable
	 * exception = null; try { dockerClient.stopContainer(containerID, seconds);
	 * return Response.ok("ok").build(); } catch (DockerException |
	 * InterruptedException e) { exception = e; e.printStackTrace(); } finally {
	 * dockerClient.close(); } return
	 * Response.serverError().entity(ExceptionUtils
	 * .getStackTrace(exception)).build(); }
	 */

	public boolean check() {
		return ip.contains(":");
	}

	public String getIp() {
		return ip;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean available() {
		return true;
	}
}
