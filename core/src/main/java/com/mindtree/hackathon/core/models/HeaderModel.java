package com.mindtree.hackathon.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.REQUIRED)
public class HeaderModel {

	@Inject
	protected String title;

	@Inject
	protected String logo;

	@Inject
	protected String style;

	private List<NavLinkModel> links;

	@SlingObject
	private Resource currentResource;

	@PostConstruct
	protected void init() {
		Resource linksResource = currentResource.getChild("./links");
		links = new ArrayList<>();
		linksResource.getChildren().forEach(res -> links.add(res.adaptTo(NavLinkModel.class)));
	}

	public String getTitle() {
		return title;
	}

	public String getLogo() {
		return logo;
	}

	public String getStyle() {
		return style;
	}

	public List<NavLinkModel> getLinks() {
		return links;
	}

}