package com.fms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fms.model.EventDetail;

public interface EventDetailsRepository extends MongoRepository<EventDetail, String> {

	List<EventDetail> findByeventIdIn(List<String> eventIds);

	EventDetail findByemployeeId(int employeeId);
}
