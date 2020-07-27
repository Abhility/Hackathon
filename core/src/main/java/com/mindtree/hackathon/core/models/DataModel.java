package com.mindtree.hackathon.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class)
public class DataModel {
	
	@SlingObject
	private Resource currentResource;

	private List<UserModel> users;
	
	@PostConstruct
	void init() {
		Resource userResource = currentResource.getChild("./users");
		users = new ArrayList<>();
		userResource.getChildren().forEach(resource -> users.add(resource.adaptTo(UserModel.class)));
	}

	public List<UserModel> getUsers() {
		return users;
	}


}
