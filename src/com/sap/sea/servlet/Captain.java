package com.sap.sea.servlet;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.common.collect.ImmutableList;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.Image;

@Path("/captain")
public class Captain {

	@GET
	@Path("/test")
	@Produces("text/plain")
	public String test(){
		/*DockerClient dockerClient= new DefaultDockerClient("http://10.58.136.164:4567");
		
		try {
			List<Image> images = dockerClient.listImages();
			
			for (Image image : images) {
				ImmutableList<String> tags = image.repoTags();
				for (String tag : tags) {
					System.out.println(tag);
				};
			}
			return images.toString();
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
		} finally{
			dockerClient.close();
		}*/
		return "avd";
	}
}
