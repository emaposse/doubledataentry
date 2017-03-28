/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.doubledataentry.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.DoubleDataEntryConstants;
import org.openmrs.module.doubledataentry.Participant;
import org.openmrs.module.doubledataentry.api.DoubleDataEntryService;
import org.openmrs.module.doubledataentry.api.dao.ConfigurationDao;
import org.openmrs.module.doubledataentry.api.dao.DoubleDataEntryDao;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.springframework.transaction.annotation.Transactional;
import sun.awt.ConstrainableGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoubleDataEntryServiceImpl extends BaseOpenmrsService implements DoubleDataEntryService {
	
	DoubleDataEntryDao dao;
	
	UserService userService;
	
	ConfigurationDao configurationDao;
	
	HtmlFormEntryService htmlFormEntryService;
	
	AdministrationService adminService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DoubleDataEntryDao dao) {
		this.dao = dao;
	}
	
	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}
	
	public void setHtmlFormEntryService(HtmlFormEntryService htmlFormEntryService) {
		this.htmlFormEntryService = htmlFormEntryService;
	}
	
	public void setAdminService(AdministrationService adminService) {
		this.adminService = adminService;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public List<HtmlForm> searchHtmlFormsByName(final String search) {
		return dao.searchHtmlForms(search);
	}
	
	@Override
	public List<Configuration> getAllConfigurations() {
		return getAllConfigurations(false);
	}
	
	@Override
	public List<Configuration> getAllConfigurations(Boolean includeRetired) {
		return configurationDao.getAllConfigurations(includeRetired);
	}
	
	@Override
	public List<Participant> getAllParticipants() {
		return new ArrayList<Participant>();
	}
	
	@Override
	public List<HtmlForm> getAllHtmlFormsHavingConfigurations() {
		return dao.getHtmlFormsUsedInConfigurations();
	}
	
	@Override
	public Configuration createConfigurationFromMap(Map<String, Object> configurationMap) {
		Configuration configuration = new Configuration();
		HtmlForm form = null;
		String formUuid = (String) configurationMap.get("formUuid");
		if (formUuid == null) {
			Object formId = configurationMap.get("formId");
			if (formId == null) {
				throw new APIException("map should have either formUuid or formId as keys with proper values");
			} else {
				form = htmlFormEntryService.getHtmlForm(Integer.valueOf(formId.toString()));
				if (form == null) {
					throw new APIException("No HTML form associated with id " + formId + " found");
				}
			}
		} else {
			form = htmlFormEntryService.getHtmlFormByUuid(formUuid);
			if (form == null) {
				throw new APIException("No HTML form associated with id " + formUuid + " found");
			}
		}
		
		configuration.setHtmlForm(form);
		Object frequency = configurationMap.get("frequency");
		if (frequency == null) {
			String gp = adminService.getGlobalProperty(DoubleDataEntryConstants.DEFAULT_FREQUENCY_GP);
			configuration.setFrequency(Float.valueOf(gp));
		} else {
			configuration.setFrequency(Float.valueOf(frequency.toString()));
		}
		
		Object published = configurationMap.get("published");
		if (published != null) {
			configuration.setPublished(Boolean.valueOf(published.toString()));
		} else {
			configuration.setPublished(Boolean.FALSE);
		}
		
		return configurationDao.saveConfiguration(configuration);
	}
	
	@Override
	@Transactional
	public List<Configuration> createConfigurationsFromMaps(List<Map<String, Object>> configurationMaps) {
		List<Configuration> configurations = new ArrayList<Configuration>();
		Configuration configuration = null;
		for (Map<String, Object> objectMap : configurationMaps) {
			configuration = createConfigurationFromMap(objectMap);
			configurations.add(configurationDao.saveConfiguration(configuration));
		}
		return configurations;
	}
}
