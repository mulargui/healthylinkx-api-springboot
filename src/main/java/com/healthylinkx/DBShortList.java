package com.healthylinkx;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;

import java.sql.PreparedStatement;  
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

@Repository
public class DBShortList {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Pair getContent( String npi1, String npi2, String npi3){

		//sql string
		String sql = "INSERT INTO transactions VALUES (DEFAULT,DEFAULT,?,?,?)";
		
		KeyHolder holder = new GeneratedKeyHolder();

		try {
			jdbcTemplate.update( con -> {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //new String[]{"ID"}
				ps.setString(1, npi1);
				ps.setString(2, npi2);
				ps.setString(3, npi3);
				return ps;
			}, holder);
		} catch (DataAccessException e) {
			return new Pair();
		}

		//get the generated transaction id
		Pair ret = new Pair();
		ret.transactionId = holder.getKey().toString();
		
		//return detailed info of the providers
		String querystring = "SELECT NPI,Provider_Full_Name,Provider_Full_Street, Provider_Full_City, Provider_Business_Practice_Location_Address_Telephone_Number FROM npidata2 WHERE ((NPI = '"
								+ npi1 + "')";
		if(!Utils.isBlank(npi2))
			querystring += "OR (NPI = '" + npi2 + "')";
		if(!Utils.isBlank(npi3))
			querystring += "OR (NPI = '" + npi3 + "')";
		querystring += ")";
	
		List<String[]> mylist;
		// get the details of NPI's
		try {
			ret.npilist = jdbcTemplate.query( 
				querystring,
				(rs, rowNum) -> new String[]{rs.getString("NPI")
					,rs.getString("Provider_Full_Name")
					,rs.getString("Provider_Full_Street")
					,rs.getString("Provider_Full_City")
					,rs.getString("Provider_Business_Practice_Location_Address_Telephone_Number")
				});
			return ret;
		} catch (EmptyResultDataAccessException e) {
			return new Pair();
		}
	}
}