package com.healthylinkx;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonParser;

import java.util.List;
import java.io.IOException;

@Repository
public class Transaction {
	
	@Autowired
	private DBTransaction mydb;

	String service(String id){
		
		 //check params
		if (Utils.isBlank(id))
			return new String();

		//get the data from the db
		List<String[]> mylist = mydb.getContent(id);		
	
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
				jGenerator.writeStringField("Provider_Business_Practice_Location_Address_Telephone_Number", s[4]);
				jGenerator.writeEndObject();
			}
			jGenerator.writeEndArray();
			jGenerator.close();
		
			return new String(stream.toByteArray(), "UTF-8");
		}catch (IOException e){
			return new String();
		}
	}
}