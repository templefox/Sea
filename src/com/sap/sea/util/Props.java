package com.sap.sea.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Props {
	private static boolean loaded = false;
	private static Properties properties = new Properties();
	
	public static Properties instance(){
		return properties;
	}
	
	
	/**
	 * Will load only once
	 * @param inStream
	 * @throws IOException
	 */
	public static void load(InputStream inStream) throws IOException{
		if (!loaded) {
			properties.load(inStream);			
			loaded = true;
		}
	}
	/**
	 * Reload from file
	 * @param inStream
	 * @throws IOException
	 */
	public static void reload(InputStream inStream) throws IOException{
		loaded = false;
		load(inStream);
	}
}
