package com.keywords;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.listeners.ErrorUtil;
import com.logs.Logging;

public class VerifyDBResult {
	public String doVerifyDBResult(String object, String input, Hashtable<String, String> data){
		StringBuilder result= new StringBuilder();
		Connection conn = null;
		String url = CreatePropertiesObjects.CONFIG.getProperty("DB_URL");
		String driver = "com.mysql.jdbc.Driver";
		String db = CreatePropertiesObjects.CONFIG.getProperty("DBNAME");
		String username = CreatePropertiesObjects.CONFIG.getProperty("DB_USERNAME");
		String password = CreatePropertiesObjects.CONFIG.getProperty("DB_PASSWORD");;

		String inputs[] = input.split(",");
		int i = 1;
		try{
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+db, username, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(object);
			
			//PreparedStatement pstmt = conn.prepareStatement("select * from packages where package_id = ?");
			//pstmt.setString(1, "256");
			//ResultSet rs1 = pstmt.executeQuery();
			while(rs.next()){
				while(i <= inputs.length){
					if(rs.getString(i).equalsIgnoreCase(inputs[i-1])){
						result.append(String.format("PASS -  input value %s matches with corresponding value in db %s", inputs[i-1],rs.getString(i) )) ;
						result.append('\n');
					}
					else{
						result.append(String.format("FAIL -  input value %s does not match with corresponding value in db %s", inputs[i-1],rs.getString(i) ));
						result.append('\n');
						Logging.log(String.format("FAIL -  input value %s does not match with corresponding value in db %s", inputs[i-1],rs.getString(i) ));
						ErrorUtil.addVerificationFailure(new CustomException(String.format("FAIL -  input value %s does not match with corresponding value in db %s", inputs[i-1],rs.getString(i) )));
					}
					i++;
				}
				
				
				//System.out.println(rs.getString(1) +"---" + rs.getString(2) + "----" + rs.getString(3));
			}
		}catch(Exception e){
			ErrorUtil.addVerificationFailure(e);
		}
		finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				ErrorUtil.addVerificationFailure(new CustomException(String.format("Unable to close DB connection with db - %s, connection string - %s",db, url)));
			}
		}
	
		return result.toString();
	}
}
