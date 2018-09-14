package com.lteu.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RESTAPIConnect {

	private String BaseUrl;
	private String token;
	
	public RESTAPIConnect(String url) {
		this.BaseUrl = url;
	}
	
	public String GetOpenCellID(String lac, String cid) throws MalformedURLException, IOException {
	
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL(BaseUrl).openConnection()));
		httpcon.setDoOutput(true);	
		httpcon.setRequestProperty("Content-Type", "application/json");
		httpcon.setRequestProperty("Accept", "application/json");
		httpcon.setRequestMethod("POST");
		httpcon.connect();
		byte[] outputBytes = ("{\"token\": \"881322550b806c\",\"radio\": \"lte\",\"mcc\": 310,\"mnc\": 404,\"cells\": [{\"lac\": "+ lac +",\"cid\": " + cid + ", \" psc\": 0}], \"address\":1}").getBytes("UTF-8");
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();
		BufferedReader inreader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		StringBuilder content = new StringBuilder();
		String line;
		while (null != (line = inreader.readLine())) {
		    content.append(line);
		}
		System.out.println(content.toString());
        inreader.close();
	    return content.toString();
	 }
}