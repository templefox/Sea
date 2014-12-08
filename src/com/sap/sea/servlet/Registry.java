package com.sap.sea.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.sap.sea.misc.Props;
import com.sap.sea.worker.keeper.DefaultKeeper;
import com.sap.sea.worker.keeper.Keeper;

@WebServlet("/registry/json")
public class Registry extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Props.instance().load(new FileInputStream(request.getServletContext().getRealPath("WEB-INF")+"/config.properties"));
		String hub = Props.instance().getProperty("hub");
		
		Keeper keeper = new DefaultKeeper(hub);
		JsonArray array = keeper.searchAsJsonArray("library");
		response.getWriter().write(array.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
