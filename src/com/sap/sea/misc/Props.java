package com.sap.sea.misc;

import java.util.Properties;

public final class Props {
	private static Properties properties = new Properties();
	
	public static Properties instance(){
		return properties;
	}
}
