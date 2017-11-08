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
public class DBTransaction {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	List<String[]> getContent( String id ){

		//building the query string
		String query= "SELECT * FROM transactions WHERE (id = '"+ id +"')";

		// get the list of NPI's
		String[] npiList;
		try {
			npiList = jdbcTemplate.queryForObject( 
				query,
				(rs, rowNum) -> new String[]{rs.getString("NPI1")
					,rs.getString("NPI2")
					,rs.getString("NPI3")
				});
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String[]>();
		}

		//building the query string to get the details of the providers
		query = "SELECT NPI,Provider_Full_Name,Provider_Full_Street, Provider_Full_City, Provider_Business_Practice_Location_Address_Telephone_Number FROM npidata2 WHERE ((NPI = '" 
								+ npiList[0] + "')";
		if (!Utils.isBlank(npiList[1]))
			query += "OR (NPI = '" + npiList[1] +"')";
		if (!Utils.isBlank(npiList[2]))
			query += "OR (NPI = '" + npiList[2] +"')";
		query += ")";
			
		// get the details of NPI's
		try {
			return jdbcTemplate.query( 
				query,
				(rs, rowNum) -> new String[]{rs.getString("NPI")
					,rs.getString("Provider_Full_Name")
					,rs.getString("Provider_Full_Street")
					,rs.getString("Provider_Full_City")
					,rs.getString("Provider_Business_Practice_Location_Address_Telephone_Number")
				});
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String[]>();
		}
	}
}