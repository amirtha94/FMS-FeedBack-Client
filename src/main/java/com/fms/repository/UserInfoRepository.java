package com.fms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fms.model.User;

public interface UserInfoRepository extends MongoRepository<User, String>{

	public List<User> findAllByrole(String role);
}
