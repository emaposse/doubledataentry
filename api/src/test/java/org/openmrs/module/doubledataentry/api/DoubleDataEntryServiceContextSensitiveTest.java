/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.doubledataentry.api;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.UserService;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.api.dao.ConfigurationDao;
import org.openmrs.module.doubledataentry.api.dao.DoubleDataEntryDao;
import org.openmrs.module.doubledataentry.api.impl.DoubleDataEntryServiceImpl;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sun.security.krb5.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.when;

/**
 * This is a unit test, which verifies logic in DoubleDataEntryService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class DoubleDataEntryServiceContextSensitiveTest extends BaseModuleContextSensitiveTest {
	
	private static final String DEFAULT_FREQUENCY = "0.1";
	
	@Autowired
	@Qualifier("doubledataentry.DoubleDataEntryService")
	DoubleDataEntryService ddeService;
	
	@Before
	public void setup() throws Exception {
		executeDataSet("test-data.xml");
	}
	
	@Test
	public void retireConfiguration_shouldRetireConfigurationGivenReason() {
		Configuration configuration = new Configuration();
		String reason = "Let us see if this works actually";
		configuration = ddeService.retireConfiguration(configuration, reason);
		
		assertTrue("configuration should be retired", configuration.getRetired());
		assertEquals("Reason should be the same", reason, configuration.getRetireReason());
		assertNotNull("Date Retired should be set", configuration.getDateRetired());
		assertNotNull("Retired user should be set", configuration.getRetiredBy());
	}
	
	@Test
	public void retireConfigurationByUuid_shouldRetireConfiguration() {
		String uuid = "a7a41f9d-0b38-11e7-b6f1-0242ac110002";
		String reason = "retire this darn thing!";
		
		Configuration configuration = ddeService.retireConfigurationByUuid(uuid, reason);
		
		assertTrue("configuration should be retired", configuration.getRetired());
		assertEquals("Reason should be the same", reason, configuration.getRetireReason());
		assertNotNull("Date Retired should be set", configuration.getDateRetired());
		assertNotNull("Retired user should be set", configuration.getRetiredBy());
	}
}
