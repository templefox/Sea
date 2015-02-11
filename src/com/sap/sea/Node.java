package com.sap.sea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class Node {
	public static final String FREE_GREP_MEM_AWK_PRINT_$3 = "free | grep Mem: | awk  '{print $3}'";
	public static final String FREE_GREP_MEM_AWK_PRINT_$2 = "free | grep Mem: | awk  '{print $2}'";
	public static final String FREE_GREP_MEM_AWK_PRINT_$2_$3 = "free | grep Mem: | awk  '{print $3/$2 }'";
	public static final String FREE_GREP_MEM_AWK_PRINT_$4 = "free | grep Mem: | awk  '{print $4 }'";

	public Connection connection;

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
	public Response memUsage() {
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$2_$3)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}

	@GET
	@Path("mem/total")
	public Response memTotal() {
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$2)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}

	@GET
	@Path("mem/used")
	public Response memUsed() {
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$3)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}

	@GET
	@Path("mem/free")
	public Response memFree() {
		try {
			return Response.ok(runSh(FREE_GREP_MEM_AWK_PRINT_$4)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}

	@GET
	@Path("hostname")
	public Response hostname() {
		try {
			return Response.ok(runSh("hostname")).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
	
	@GET
	@Path("checkurl")
	public Response checkurl(@QueryParam("url") final String url){
		try {
			String command = "curl -k -X HEAD " + url + " | grep curl: | awk  '{print $3}'";
			return Response.ok(runSh(command)).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity(ExceptionUtils.getStackTrace(e)).build();
		}
	}
	

	@POST
	@Path("build")
	public void build(final String buildPath, @QueryParam("name") final String name,
			@Suspended final AsyncResponse asyncResponse) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String a = runSh("cd " + buildPath + " && docker build -t " + name + " .");
					asyncResponse.resume(a);
				} catch (IOException e) {
					e.printStackTrace();
					asyncResponse.resume(e);
				}
			}
		}).start();

	}

	@POST
	@Path("save")
	public void save(@QueryParam("image") final String image, @QueryParam("file") final String file,
			@Suspended final AsyncResponse asyncResponse) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String a = runSh("docker save -o " + file + " " + image);
					asyncResponse.resume(a);
				} catch (IOException e) {
					e.printStackTrace();
					asyncResponse.resume(e);
				}
			}
		}).start();
	}

	@POST
	@Path("load")
	public void load(@QueryParam("file") final String file, @Suspended final AsyncResponse asyncResponse) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String a = runSh("docker load -i " + file);
					asyncResponse.resume(a);
				} catch (IOException e) {
					e.printStackTrace();
					asyncResponse.resume(e);
				}
			}
		}).start();
	}

	public String runSh(String sh) throws IOException {
		String ip = island.getIp();
		boolean auth = false;
		if (connection == null) {
			try {
				connection = new Connection(ip.substring(0, ip.indexOf(":")));
				connection.connect();
				// auth = connection.authenticateWithPassword(island.getUser(),
				// island.getPass());
				String ppk = Sea.getJedis().get("private_key");
				auth = connection.authenticateWithPublicKey(island.getUser(), ppk.toCharArray(), "");
			} catch (IOException e) {
				e.printStackTrace();
				connection = null;
				throw new IOException("Please config the /etc/ssh/sshd_config and restart sshd", e);
			}
			if (!auth) {
				connection = null;
				throw new AuthenticationException("Wrong password");
			}
		}

		Session session = null;
		try {
			session = connection.openSession();
		} catch (IllegalStateException e) {
			connection = null;
			e.printStackTrace();
			throw new AuthenticationException("Unable to authenticate");
		}

		session.execCommand(sh);

		InputStream stdoutInputStream = new StreamGobbler(session.getStdout());
		BufferedReader stdoutBufferedReader = new BufferedReader(new InputStreamReader(stdoutInputStream));

		InputStream stderrInputStream = new StreamGobbler(session.getStderr());
		BufferedReader stderrBufferedReader = new BufferedReader(new InputStreamReader(stderrInputStream));

		StringBuilder builder = new StringBuilder();
		String firstLine = stdoutBufferedReader.readLine();
		if (!Strings.isNullOrEmpty(firstLine)) {
			builder.append(firstLine);
		}

		int blankTime = 5;
		while (true) {
			String line = stdoutBufferedReader.readLine();
			if (line == null && blankTime == 0)
				break;
			if (line != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}if(line==null){
			--blankTime;
			}
		}

		blankTime = 5;
		while (true) {
			String line = stderrBufferedReader.readLine();
			if (line == null && blankTime == 0)
				break;
			if (line != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			if(line == null){
			--blankTime;
			}
		}

		stdoutBufferedReader.close();
		stderrBufferedReader.close();
		session.close();

		return builder.toString();
	}

}
