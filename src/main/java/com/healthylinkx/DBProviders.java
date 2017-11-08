package com.healthylinkx;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.List;
import java.util.ArrayList;

@Repository
public class DBProviders {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	List<String[]> getContent( List<String> zipcodes, String gender, 
		String lastname1, String lastname2, String lastname3, 
		String specialty
	){
		//building the query string
		String query= "SELECT NPI,Provider_Full_Name,Provider_Full_Street,Provider_Full_City FROM npidata2 WHERE (";
			
		if(!Utils.isBlank(lastname1))
			query += "((Provider_Last_Name_Legal_Name = '" + lastname1 + "')";
		if(!Utils.isBlank(lastname2))
			query += " OR (Provider_Last_Name_Legal_Name = '" + lastname2 + "')";
		if(!Utils.isBlank(lastname3))
			query += " OR (Provider_Last_Name_Legal_Name = '" + lastname3 + "')";
		if(!Utils.isBlank(lastname1))
			query += ")";
		if(!Utils.isBlank(gender)){
			if(!Utils.isBlank(lastname1))
				query += " AND ";
			query += "(Provider_Gender_Code = '" + gender + "')";
		}
		if(!Utils.isBlank(specialty)){
			if(!Utils.isBlank(lastname1) || !Utils.isBlank(gender))
				query += " AND ";
			query += "(Classification = '" + specialty + "')";
		}
 		if(!zipcodes.isEmpty()){
			if(!Utils.isBlank(lastname1) || !Utils.isBlank(gender) || !Utils.isBlank(specialty))
				query += " AND ";
			query += "((Provider_Short_Postal_Code = '"+ zipcodes.get(0) +"')";
			for (int i = 1; i < zipcodes.size(); i++)
				query += " OR (Provider_Short_Postal_Code = '"+ zipcodes.get(i) +"')";
			query += ")";
		}
  		query += ") limit 50";
						
		try {
			return jdbcTemplate.query( 
				query,
				(rs, rowNum) -> new String[]{rs.getString("NPI")
					,rs.getString("Provider_Full_Name")
					,rs.getString("Provider_Full_Street")
					,rs.getString("Provider_Full_City")
				});
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String[]>();
		}
	}
}