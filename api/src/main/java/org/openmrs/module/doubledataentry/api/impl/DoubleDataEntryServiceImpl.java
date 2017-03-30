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

import org.hibernate.ObjectNotFoundException;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.ConfigurationRevision;
import org.openmrs.module.doubledataentry.DoubleDataEntryConstants;
import org.openmrs.module.doubledataentry.Participant;
import org.openmrs.module.doubledataentry.api.DoubleDataEntryService;
import org.openmrs.module.doubledataentry.api.dao.ConfigurationDao;
import org.openmrs.module.doubledataentry.api.dao.DoubleDataEntryDao;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DoubleDataEntryServiceImpl extends BaseOpenmrsService implements DoubleDataEntryService {
	
	DoubleDataEntryDao dao;
	
	UserService userService;
	
	ConfigurationDao configurationDao;
	
	HtmlFormEntryService htmlFormEntryService;
	
	AdministrationService adminService;
	
	MessageSourceService messageSourceService;
	
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
	
	public void setMessageSourceService(MessageSourceService messageSourceService) {
		this.messageSourceService = messageSourceService;
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
	public Configuration getConfigurationById(Integer id) {
		return configurationDao.getConfiguration(id);
	}
	
	@Override
	public Configuration getConfigurationByUuid(String uuid) {
		Configuration configuration = configurationDao.getConfiguration(uuid);
		if (configuration == null) {
			throw new ObjectNotFoundException(Configuration.class, "Configuration with uuid " + uuid + " not found");
		}
		return configuration;
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
	public List<ConfigurationRevision> getConfigurationRevisionsForConfiguration(Configuration configuration) {
		return configurationDao.getConfigurationRevisionsForConfiguration(configuration);
	}
	
	@Override
	public List<Participant> getAllParticipants() {
		return new ArrayList<Participant>();
	}
	
	@Override
	public List<HtmlForm> getAllHtmlFormsHavingConfigurations() {
		return getAllHtmlFormsHavingConfigurations(false, false);
	}
	
	@Override
	public List<HtmlForm> getAllHtmlFormsHavingConfigurations(Boolean includeRetired, Boolean includeRetiredConfigurations) {
		return dao.getHtmlFormsUsedInConfigurations(includeRetired, includeRetiredConfigurations);
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
	public Configuration updateConfigurationFromMap(Map<String, Object> configurationMap) {
		String uuid = (String) configurationMap.get("uuid");
		Configuration configuration = configurationDao.getConfiguration(uuid);
		
		Object frequency = configurationMap.get("frequency");
		ConfigurationRevision revision = null;
		if (frequency != null) {
			revision = new ConfigurationRevision();
			revision.setFrequency(configuration.getFrequency());
			revision.setEndDate(new Date());
			ConfigurationRevision lastRevision = configurationDao.getLastRevisionForConfiguration(configuration);
			
			if (lastRevision != null) {
				revision.setStartDate(lastRevision.getEndDate());
			} else {
				revision.setStartDate(configuration.getDateCreated());
			}
			
			configuration.setFrequency(Float.valueOf(frequency.toString()));
		}
		
		Object published = configurationMap.get("published");
		if (published != null) {
			configuration.setPublished(Boolean.valueOf(published.toString()));
			
		}
		
		if (revision != null) {
			configuration.revise();
			configuration = configurationDao.saveConfiguration(configuration);
			revision.setConfiguration(configuration);
			configurationDao.saveConfigurationRevision(revision);
			
			return configuration;
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
			configurations.add(configuration);
		}
		return configurations;
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Configuration retireConfiguration(Configuration configuration, String reason) {
		if (!StringUtils.hasText(reason)) {
			throw new IllegalArgumentException(messageSourceService.getMessage("general.voidReason.empty"));
		}
		
		if (!configuration.getRetired()) {
			return configurationDao.saveConfiguration(configuration);
		}
		return configuration;
	}
	
	@Override
	@Transactional
	public Configuration retireConfigurationByUuid(String uuid, String reason) {
		Configuration configuration = configurationDao.getConfiguration(uuid);
		
		return Context.getService(DoubleDataEntryService.class).retireConfiguration(configuration, reason);
	}
}
