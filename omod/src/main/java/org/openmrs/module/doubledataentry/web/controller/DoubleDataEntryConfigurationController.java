/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.doubledataentry.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.UserService;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.DoubleDataEntryConstants;
import org.openmrs.module.doubledataentry.api.DoubleDataEntryService;
import org.openmrs.module.doubledataentry.web.RestUtils;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("doubledataentry.ConfigurationController")
@RequestMapping(value = "/module/doubledataentry/configuration")
public class DoubleDataEntryConfigurationController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	UserService userService;
	
	@Autowired
	DoubleDataEntryService ddeService;
	
	@Autowired
	HtmlFormEntryService htmlFormEntryService;
	
	@Autowired
	@Qualifier("adminService")
	private AdministrationService as;
	
	/**
	 * Initially called after the getUsers method to get the landing form name
	 * 
	 * @return String form view name
	 */
	@RequestMapping(value = "manage.form", method = RequestMethod.GET)
	public ModelAndView onGet() {
		ModelAndView toRet = new ModelAndView("/module/doubledataentry/manage-configuration");
		
		// Get existing configuration.
		String defaultFrequency = as.getGlobalProperty(DoubleDataEntryConstants.DEFAULT_FREQUENCY_GP, "0.1");
		
		toRet.addObject("configurations", ddeService.getAllConfigurations());
		toRet.addObject("participants", ddeService.getAllParticipants());
		toRet.addObject("defaultFrequency", defaultFrequency);
		return toRet;
	}
	
	@RequestMapping(value = "forms")
	@ResponseBody
	public Object searchForms(@RequestParam("search") String search,
	        @RequestParam(value = "filterUsed", defaultValue = "true") boolean filter) {
		List<HtmlForm> matches = ddeService.searchHtmlFormsByName(search);
		if (filter) {
			matches.removeAll(ddeService.getAllHtmlFormsHavingConfigurations());
		}
		return convertListOfHtmlFormsToListOfMaps(matches);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object onConfigurationsPost(@RequestBody List confMaps) {
		List<Configuration> configurations = ddeService.createConfigurationsFromMaps(confMaps);
		return convertListOfConfigurationsToListOfMaps(configurations);
	}
	
	/**
	 * This class returns the form backing object. This can be a string, a boolean, or a normal java
	 * pojo. The bean name defined in the ModelAttribute annotation and the type can be just defined
	 * by the return type of this method
	 */
	@ModelAttribute("users")
	protected List<User> getUsers() throws Exception {
		List<User> users = userService.getAllUsers();
		
		// this object will be made available to the jsp page under the variable name
		// that is defined in the @ModuleAttribute tag
		return users;
	}
	
	protected List<Map<String, Object>> convertListOfHtmlFormsToListOfMaps(List<HtmlForm> htmlForms) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (HtmlForm f : htmlForms) {
			list.add(RestUtils.convertHtmlFormToMap(f));
		}
		return list;
	}
	
	protected List<Map<String, Object>> convertListOfConfigurationsToListOfMaps(List<Configuration> configurations) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Configuration c : configurations) {
			list.add(RestUtils.convertConfigurationToMap(c));
		}
		return list;
	}
}
