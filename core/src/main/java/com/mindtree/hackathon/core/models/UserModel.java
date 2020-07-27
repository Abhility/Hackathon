package com.mindtree.hackathon.core.models;

import java.util.Date;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class UserModel {

	@Inject
	protected String name;

	@Inject
	protected String email;

	@Inject
	protected Long age;

	@Inject
	protected String color;

	@Inject
	protected Date birthdate;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Long getAge() {
		return age;
	}

	public String getColor() {
		return color;
	}

	public Date getBirthdate() {
		return birthdate;
	}
	
}
