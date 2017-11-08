package com.healthylinkx;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
public class MainController {
		
	@Autowired
	private Taxonomy tx;

	@Autowired
	private Providers pv;

	@Autowired
	private Transaction ts;

	@Autowired
	private ShortList sl;

	@GetMapping("/taxonomy")
	public String taxonomy() {
		//service the request
		return  tx.service();
	}

	@GetMapping( "/providers")
	public String providers(
		@RequestParam(value = "zipcode",   required = false) String zipcode,
		@RequestParam(value = "gender",    required = false) String gender,
		@RequestParam(value = "lastname1", required = false) String lastname1,
		@RequestParam(value = "lastname2", required = false) String lastname2,
		@RequestParam(value = "lastname3", required = false) String lastname3,
		@RequestParam(value = "specialty", required = false) String specialty,
		@RequestParam(value = "distance",  required = false) String distance
		){
		//service the request
		return  pv.service(zipcode, gender, lastname1, lastname2, lastname3, specialty, distance);
	}
	
	@GetMapping("/shortlist")
	public String shortlist(
		@RequestParam(value = "NPI1", required = false) String NPI1,
		@RequestParam(value = "NPI2", required = false) String NPI2,
		@RequestParam(value = "NPI3", required = false) String NPI3
		){
		//service the request
		return  sl.service(NPI1, NPI2, NPI3);
	}
	
	@GetMapping("/transaction")
	public String transaction(
		@RequestParam("id") String id		
		){
		//service the request
		return  ts.service(id);
	}
}
