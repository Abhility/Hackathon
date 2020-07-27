package com.mindtree.hackathon.core.models;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.REQUIRED)
public class NavLinkModel {

	@Inject
	@Named("linkname")
	protected String linkName;

	@Inject
	@Named("linkpath")
	protected String linkPath;

	public String getLinkName() {
		return linkName;
	}

	public String getLinkPath() {
		return linkPath;
	}

}