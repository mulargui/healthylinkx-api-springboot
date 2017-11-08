package com.healthylinkx;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonEncoding;

@Repository
public class Taxonomy {
	
	@Autowired
	private DBTaxonomy mydb;
	
	public String service() {
		//get the data
		List<String> mylist = mydb.getContent();
		
		/* old version, it works
		//build the json response
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(mylist);
		}catch (JsonMappingException e){
			return new String();
		}catch (JsonGenerationException e){
			return new String();
		}catch (IOException e){
			return new String();
		}
		//return String.join("#", mylist);
		*/
		
		//build the json response
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			JsonFactory jfactory = new JsonFactory();
			JsonGenerator jGenerator = jfactory
				.createGenerator(stream, JsonEncoding.UTF8);
		
			jGenerator.writeStartArray();
			for (String s: mylist){
				jGenerator.writeStartObject();
				jGenerator.writeStringField("Classification", s);
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