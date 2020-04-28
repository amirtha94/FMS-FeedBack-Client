package com.fms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fms.model.EmailData;
import com.fms.service.FMSServiceUtil;

@RestController
public class EmailController {

	@Autowired
	FMSServiceUtil service;

	@GetMapping("/email")
	public void sendEmailToAllParticipant() {
		service.sendEmailToParticipant();

	}

	@PostMapping("/email")
	public ResponseEntity<String> sendEmailToBU(@RequestBody EmailData emailData) throws Exception {
		String message = service.sendEmailToBU(emailData);
		return new ResponseEntity<String>(message, HttpStatus.OK);

	}

}
