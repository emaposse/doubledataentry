package org.openmrs.module.doubledataentry.api.dao;

/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConfigurationDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private ConfigurationDao dao;
	
	@Autowired
	private HtmlFormEntryService htmlFormEntryService;
	
	private HtmlForm form;
	
	private static final String testFile = "test-data.xml";
	
	@Before
	public void setup() throws Exception {
		form = htmlFormEntryService.getHtmlForm(1);
		executeDataSet(testFile);
	}
	
	@Test
	public void saveConfiguration_shouldSaveConfigurationToDatabase() throws Exception {
		Configuration configuration = new Configuration(form, 0.1f);
		
		assertNull("id is not yet set", configuration.getId());
		
		// Save the thing
		configuration = dao.saveConfiguration(configuration);
		assertNotNull("id should be set", configuration.getId());
	}
	
	@Test
	public void getConfiguration_shouldReturnConfigurationGivenId() throws Exception {
		Configuration configuration = dao.getConfiguration(1);
		assertNotNull("configuration object should not be null", configuration);
		assertEquals("uuid of fetched object", "a7a41f9d-0b38-11e7-b6f1-0242ac110002", configuration.getUuid());
	}
	
	@Test
	public void getConfiguration_shouldReturnConfigurationGivenUuid() throws Exception {
		Configuration configuration = dao.getConfiguration("a7a41f9d-0b38-11e7-b6f1-0242ac110002");
		assertNotNull("configuration object should not be null", configuration);
		assertEquals("id of fetched object", Integer.valueOf(1), configuration.getId());
	}
}
