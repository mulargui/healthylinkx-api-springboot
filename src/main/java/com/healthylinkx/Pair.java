package com.healthylinkx;

import java.util.List;

class Pair {
	public String transactionId;
	public List<String[]> npilist;
	
	public boolean isEmpty(){
		return Utils.isBlank(transactionId);
	}
}