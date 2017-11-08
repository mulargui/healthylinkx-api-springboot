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
public class ShortList {

	@Autowired
	private DBShortList mydb;
	
	String service(String npi1, String npi2, String npi3){
		
		//check params
		if (Utils.isBlank(npi1))
			return new String();

		//get the data from the db
		Pair ret = mydb.getContent(npi1,npi2,npi3);		
	
		//build the json response
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			JsonFactory jfactory = new JsonFactory();
			JsonGenerator jGenerator = jfactory
				.createGenerator(stream, JsonEncoding.UTF8);
		
			jGenerator.writeStartObject();
			//first element is the transaction id
			jGenerator.writeStringField("Transaction", ret.transactionId);
			//this is the list of providers
			jGenerator.writeFieldName("Providers");
			jGenerator.writeStartArray();
			for (String[] s: ret.npilist){
				jGenerator.writeStartObject();
				jGenerator.writeStringField("NPI", s[0]);
				jGenerator.writeStringField("Provider_Full_Name", s[1]);
				jGenerator.writeStringField("Provider_Full_Street", s[2]);
				jGenerator.writeStringField("Provider_Full_City", s[3]);
				jGenerator.writeStringField("Provider_Business_Practice_Location_Address_Telephone_Number", s[4]);
				jGenerator.writeEndObject();
			}
			jGenerator.writeEndArray();
			jGenerator.writeEndObject();
			jGenerator.close();
		
			return new String(stream.toByteArray(), "UTF-8");
		}catch (IOException e){
			return new String();
		}
	}
}