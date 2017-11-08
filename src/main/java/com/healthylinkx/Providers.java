package com.healthylinkx;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonParser;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;

@Repository
public class Providers {

	@Autowired
	private DBProviders mydb;

	private ArrayList<String> zipcodes;

	String service(String zipcode, String gender, 
		String lastname1, String lastname2, String lastname3, 
		String specialty, String distance
	){		
		 //check params
		if (Utils.isBlank(zipcode) && Utils.isBlank(lastname1) && Utils.isBlank(specialty))
			return new String();

		//find zipcodes at a distance
		zipcodes = new ArrayList<String>();
		if (!Utils.isBlank(distance) && !Utils.isBlank(zipcode)){
			if (!getListOfZipcodes(zipcode, distance))return new String();
		}else{
			zipcodes.add(zipcode);
		}
		
		//get the data from the db
		List<String[]> mylist = mydb.getContent(zipcodes,gender,lastname1,lastname2,lastname3,specialty);		
	
		if (mylist.isEmpty()) return new String();
		
		//build the json response
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			JsonFactory jfactory = new JsonFactory();
			JsonGenerator jGenerator = jfactory
				.createGenerator(stream, JsonEncoding.UTF8);
		
			jGenerator.writeStartArray();
			for (String[] s: mylist){
				jGenerator.writeStartObject();
				jGenerator.writeStringField("NPI", s[0]);
				jGenerator.writeStringField("Provider_Full_Name", s[1]);
				jGenerator.writeStringField("Provider_Full_Street", s[2]);
				jGenerator.writeStringField("Provider_Full_City", s[3]);
				jGenerator.writeEndObject();
			}
			jGenerator.writeEndArray();
			jGenerator.close();
		
			return new String(stream.toByteArray(), "UTF-8");
		}catch (IOException e){
			return new String();
		}
	}
	
	private boolean getListOfZipcodes(String zipcode, String distance){
		try{
			URL url = new URL("http://www.zipcodeapi.com/rest/GFfN8AXLrdjnQN08Q073p9RK9BSBGcmnRBaZb8KCl40cR1kI1rMrBEbKg4mWgJk7/radius.json/" 
							+ zipcode + "/" + distance + "/mile");	
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if( conn.getResponseCode()!= HttpURLConnection.HTTP_OK) return false;
		
			//read the response
			BufferedReader rd  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null)
				sb.append(line + '\n');

			//scan the json response
			JsonFactory jfactory = new JsonFactory();
			JsonParser jParser = jfactory.createParser(sb.toString());

			while (jParser.nextToken() != null)
				if ("zip_code".equals(jParser.getCurrentName())) {
						jParser.nextToken();
						zipcodes.add(jParser.getText());
				}
			jParser.close();
		} catch (MalformedURLException  e) {
			return false;
		} catch (ProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}		
		return true;
	}
}