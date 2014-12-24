package com.sap.sea;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Island {
	private String ip;
	private String user;
	private String pass;
	
	@JsonIgnore
	private Docker docker;

	@JsonIgnore
	private Node node;
	
	private boolean enableShell = false;
	
	public Island() {
		docker = new Docker(this);
		node = new Node(this);
	}

	@Path("/docker/")
	public Docker getDocker(){
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
			return Integer.valueOf(free.replace(System.getProperty("line.separator"), ""))>mem*1024;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
