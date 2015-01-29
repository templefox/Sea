package com.sap.sea;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;






import com.fasterxml.jackson.annotation.JsonIgnore;

public class Island {
	private String ip;
	private String user;
	private String pass;

	private static Map<Integer,String> uids = new HashMap<Integer,String>();
	private static Random random = new Random();
	
	@JsonIgnore
	private Docker docker;

	@JsonIgnore
	private Node node;
	
	private boolean enableShell = false;
	
	public Island() {
		docker = new Docker(this);
		node = new Node(this);
	}

	@Path("/docker/{uid}/")
	public Docker getDocker(@PathParam("uid") String uid){
		if (!uid.equals("null")) {
			uids.put(new Integer(uid), ip);			
		}
		return docker;
	}
	
	@Path("/node/")
	public Node getNode(){
		if (enableShell) {
			return node;			
		}else {
			return null;
		}
	}
	
	@GET
	@Path("/ping")
	public String ping(){
		return "pong";
	}
	
	@POST
	@Path("/anchor")
	public Response setAnchor(){
		Integer uid = random.nextInt();
		while (uids.containsKey(uid)) {
			uid = random.nextInt();
		}
		uids.put(uid, null);
		Cookie cookie = new Cookie("uid", uid.toString());
		NewCookie newCookie = new NewCookie(cookie, "uid", 180, false);
		return Response.ok().cookie(newCookie).build();
	}
	
	@GET
	@Path("/anchor")
	public String releaseAnchor(@CookieParam("uid") Integer uid){
		return uids.remove(uid);
	}
	
	public void enableShell(boolean isEnabled){
		enableShell = isEnabled;
	}

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
		/*try {
			//String total = node.runSh(Node.FREE_GREP_MEM_AWK_PRINT_$2);
			//String used = node.runSh(Node.FREE_GREP_MEM_AWK_PRINT_$3);
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return true;
	}

	public boolean checkMem(Integer mem) {
		try {
			String free = node.runSh(Node.FREE_GREP_MEM_AWK_PRINT_$4);
			return Integer.valueOf(free)>mem;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
