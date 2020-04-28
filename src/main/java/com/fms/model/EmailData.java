package com.fms.model;

import java.util.List;

import lombok.Data;

@Data
public class EmailData {

	private String mailId;
	
	private List<String> events;	
}
