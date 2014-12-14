package com.sap.sea;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class Test {

	@GET
	@Path("test")
	public String test(){
		return "test";
	}
}
