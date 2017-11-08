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
public class DBTaxonomy {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<String> getContent(){
		try {
			return jdbcTemplate.query( 
				"SELECT * FROM taxonomy",
				(rs, rowNum) -> new String(rs.getString("Classification")));
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String>();
		}
	}
}

