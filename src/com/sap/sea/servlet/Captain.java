package com.sap.sea.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.PortBinding;
import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

@Path("/captain")
public class Captain {
	

	@GET
	@Path("/{ip}/images")
	@Produces("text/plain")
	public String images(@PathParam("ip") String ip){
		if (!Strings.isNullOrEmpty(ip)) {			
			DockerClient dockerClient= new DefaultDockerClient("http://"+ip);
			try {
				List<Image> images = dockerClient.listImages();
				for (Image image : images) {
					ImmutableList<String> tags = image.repoTags();
					for (String tag : tags) {
						System.out.println(tag);
					};
				}
				JsonElement element = new Gson().toJsonTree(images,new TypeToken<List<Image>>(){}.getType());
				return element.toString();
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
			} finally{
				dockerClient.close();
			}
		}
		return "error";
	}
	
	@GET
	@Path("/{ip}/containers")
	@Produces("text/plain")
	public String containers(@PathParam("ip") String ip, @DefaultValue("false") @QueryParam("all") String all){
		if (!Strings.isNullOrEmpty(ip)) {			
			DockerClient dockerClient= new DefaultDockerClient("http://"+ip);
			try {
				ListContainersParam param = new ListContainersParam("all", all);
				List<Container> containers = dockerClient.listContainers(param);
				for (Container container : containers) {
					List<String> names = container.names();
					for (String name : names) {
						System.out.println(name);
					};
				}
				JsonElement element = new Gson().toJsonTree(containers,new TypeToken<List<Container>>(){}.getType());
				return element.toString();
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
			} finally{
				dockerClient.close();
			}
		}
		return "error";
	}
	
	@GET
	@Path("/{ip}/{tag}/sailing")
	public String run(@PathParam("ip") String ip,@PathParam("tag") String tag){
		if (!Strings.isNullOrEmpty(ip)&&!Strings.isNullOrEmpty("tag")) {
			try {
				DockerClient dockerClient = new DefaultDockerClient("http://"+ip);
				Map<String, List<PortBinding>> portbinding = new HashMap<String, List<PortBinding>>(2);
				//portbinding.put("", new PortBinding[]{PortBinding.of(0, 0)})
				HostConfig config = HostConfig.builder().privileged(true).build();
				dockerClient.startContainer(tag,config);
			} catch (DockerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "failed";
	}
}
