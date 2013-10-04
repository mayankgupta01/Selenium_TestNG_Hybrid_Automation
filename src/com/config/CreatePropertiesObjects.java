package com.config;

import java.io.FileInputStream;
import java.util.Properties;

public class CreatePropertiesObjects {
	public static Properties OR = null;
	public static Properties CONFIG = null;
	public static Properties XL = null;
	FileInputStream fs = null;
	
	static CreatePropertiesObjects propertiesObj = null;
	
	private CreatePropertiesObjects(){
		try{
			OR = new Properties();
			fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//config//OR.properties");
			OR.load(fs);
			
			CONFIG = new Properties();
			fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//config//CONFIG.properties");
			CONFIG.load(fs);
			
			XL = new Properties();
			fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//config//XL.properties");
			XL.load(fs);
			}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void getPropertiesObjectInstance(){
		if(propertiesObj==null)
			propertiesObj = new CreatePropertiesObjects();
			}
	
	
	
}
