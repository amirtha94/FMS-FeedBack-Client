package com.fms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  {

	@Id
	private String employeeId;
	
	private String email;
	
	private String mobileNo;
	
	private String EmpName;
	
	private String username;
	
	private String password;

	private boolean enabled ;
	
	private String role;

	
}
