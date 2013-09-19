package org.github.dx88968.monitor.restlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.github.dx88968.monitor.utils.AddressUtil;
import org.github.dx88968.monitor.utils.Constants;
import org.json.simple.JSONObject;

public class JHttpAgent {

	public static void securePort(int port) throws IOException {
		if (checkAvailiability(port,1)) {
			return;
		}
		stopTracerOnPort(port);
		if (!checkAvailiability(port,3)) {
			throw new IOException("Can not secure port "+port);
		}
	}
	
	private static void stopTracerOnPort(int port) {
		String url="http://localhost:"+port+"/Control";
		excuteWithNoResponse("DELETE",url, AddressUtil.createUrlParameters(new JSONObject()));
	}
	
	private static boolean checkAvailiability(int port,int reties) {
		String url="http://localhost:"+port+"/Control";
		int count=0;
		while (count<reties) {
			String response=excute("POST",url, AddressUtil.createUrlParameters(new JSONObject()));
			if (response==null) {
				return true;
			}else{
				if(!verifyGETREsponse(response)){//other Http server has occupied this port
					return false;
				}
			}
			count++;
			try {
				Thread.sleep(Constants.RETRY_INTERVAL);
			} catch (InterruptedException e) {}
		}
		return false;
	}
	
	private static boolean verifyGETREsponse(String response) {
		//System.out.println(response);
		if (response==null) {
			return false;
		}
		return response.contains("alive");
	}
	
	
	private static String excute(String method,String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod(method);
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {
	      return null;
	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }

	private static void excuteWithNoResponse(String method,String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod(method);
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoOutput(true);
	      connection.getResponseCode();
	    } catch (Exception e) {
	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }

}
