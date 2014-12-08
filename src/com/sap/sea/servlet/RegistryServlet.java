package com.sap.sea.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.sap.sea.misc.Props;
import com.sap.sea.worker.keeper.DefaultKeeper;
import com.sap.sea.worker.keeper.Keeper;

@WebServlet(value={"/registry/images"})
public class RegistryServlet extends HttpServlet {
	String format;
	String query;
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		format = request.getParameter("format");
		query = request.getParameter("q");
		if (Strings.isNullOrEmpty(format)) {
			format = "json";
		}
		if (Strings.isNullOrEmpty(query)) {
			query = "library";
		}
		
		Props.instance().load(new FileInputStream(request.getServletContext().getRealPath("WEB-INF")+"/config.properties"));
		String hub = Props.instance().getProperty("hub");
		
		Keeper keeper = new DefaultKeeper(hub);
		JsonArray array = keeper.searchAsJsonArray(query);
		response.getWriter().write(array.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
