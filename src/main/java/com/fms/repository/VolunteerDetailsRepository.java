package com.fms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fms.model.VolunteerDetails;

public interface VolunteerDetailsRepository extends MongoRepository<VolunteerDetails, Integer>{

	VolunteerDetails findByemployeeId(int employeeId);
	
}
