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
import org.junit.Ignore;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This is a unit test, which verifies logic in DoubleDataEntryService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class DoubleDataEntryServiceTest {
	
	private static final String DEFAULT_FREQUENCY = "0.1";
	
	@InjectMocks
	DoubleDataEntryServiceImpl ddeService;
	
	@Mock
	DoubleDataEntryDao dao;
	
	@Mock
	ConfigurationDao configurationDao;
	
	@Mock
	HtmlFormEntryService htmlFormEntryService;
	
	@Mock
	AdministrationService adminService;
	
	@Mock
	UserService userService;
	
	@Mock
	private DoubleDataEntryService doubleDataEntryService;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
		
		when(adminService.getGlobalProperty(anyString())).thenReturn(DEFAULT_FREQUENCY);
		when(configurationDao.saveConfiguration(argThat(mockedConfiguration()))).thenAnswer(new Answer<Configuration>() {
			
			@Override
			public Configuration answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Configuration) args[0];
			}
		});
	}
	
	@Test
	public void createConfigurationFromMap_ShouldCreateWithPublishedFalse() {
		when(htmlFormEntryService.getHtmlForm(anyInt())).thenReturn(new HtmlForm());
		
		Map<String, Object> confMap = new HashMap<String, Object>();
		
		confMap.put("formId", 2);
		confMap.put("frequency", 0.2f);
		Configuration configuration = ddeService.createConfigurationFromMap(confMap);
		
		assertNotNull("returned config should not be null", configuration);
		assertFalse("By default configuration is not published", configuration.getPublished());
		assertEquals("Frequency should be set correctly", Float.valueOf(0.2f), configuration.getFrequency());
	}
	
	@Test
	public void createConfigurationFromMap_shouldSetDefaultFrequencyIfNotProvided() {
		when(htmlFormEntryService.getHtmlForm(anyInt())).thenReturn(new HtmlForm());
		Map<String, Object> confMap = new HashMap<String, Object>();
		
		confMap.put("formId", 2);
		confMap.put("published", true);
		Configuration configuration = ddeService.createConfigurationFromMap(confMap);
		
		assertNotNull("returned config should not be null", configuration);
		assertTrue("By default configuration is not published", configuration.getPublished());
		assertEquals("Frequency should be set correctly", Float.valueOf(DEFAULT_FREQUENCY), configuration.getFrequency());
	}
	
	@Test(expected = APIException.class)
	public void createConfigurationFromMap_shouldThrowExceptionIfNoFormProvided() {
		Map<String, Object> confMap = new HashMap<String, Object>();
		
		confMap.put("published", true);
		Configuration configuration = ddeService.createConfigurationFromMap(confMap);
	}
	
	@Test(expected = APIException.class)
	public void createConfigurationFromMap_shouldThrowExceptionIfHtmlFormIsNotFound() {
		when(htmlFormEntryService.getHtmlForm(anyInt())).thenReturn(null);
		Map<String, Object> confMap = new HashMap<String, Object>();
		
		confMap.put("published", true);
		Configuration configuration = ddeService.createConfigurationFromMap(confMap);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void getConfigurationByUuid_shouldThrowExceptionIfConfigurationNotFound() {
		String uuid = "does-not-exist";
		when(configurationDao.getConfiguration(uuid)).thenReturn(null);
		
		ddeService.getConfigurationByUuid(uuid);
	}
	
	private Matcher<Configuration> mockedConfiguration() {
		return new BaseMatcher<Configuration>() {
			
			@Override
			public boolean matches(Object o) {
				return true;
			}
			
			@Override
			public void describeTo(Description description) {
				
			}
		};
	}
}
