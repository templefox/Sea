package com.sap.sea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class Node {
	public static final String FREE_GREP_MEM_AWK_PRINT_$3 = "free | grep Mem: | awk  '{print $3}'";
	public static final String FREE_GREP_MEM_AWK_PRINT_$2 = "free | grep Mem: | awk  '{print $2}'";
	public static final String FREE_GREP_MEM_AWK_PRINT_$2_$3 = "free | grep Mem: | awk  '{print $2/$3 }'";
	private Island island;

	public Node(Island island) {
		this.island = island;
	}
	
	@GET
	@Path("/call")
	public Response call(@QueryParam("sh") String sh) {
		try {
			String str = runSh(sh);
			return Response.ok(str).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
	
	@GET
	@Path("mem/usage")
	public Response memUsage(){
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$2_$3)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
	
	@GET
	@Path("mem/total")
	public Response memTotal(){
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$2)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
	
	@GET
	@Path("mem/used")
	public Response memUsed(){
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$3)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}

	public String runSh(String sh) throws IOException {
		String  ip = island.getIp();
		Connection connection = new Connection(ip.substring(0,ip.indexOf(":")));
		connection.connect();

		connection.authenticateWithPassword(island.getUser(), island.getPass());

		Session session = connection.openSession();

		session.execCommand(sh);

		InputStream inputStream = new StreamGobbler(session.getStdout());
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		StringBuilder builder = new StringBuilder();
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			builder.append(line);
			builder.append("\r\n");
		}

		br.close();
		session.close();
		connection.close();
		return builder.toString();
	}

}
