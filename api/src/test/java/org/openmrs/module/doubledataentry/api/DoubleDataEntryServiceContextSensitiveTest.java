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

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.ConfigurationRevision;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
	
	@Test
	public void updateConfigurationFromMap_shouldUpateConfigurationWithoutRevisingIfFrequencyIsNotChanged() {
		Map<String, Object> map = new HashMap<String, Object>();
		Configuration configuration = ddeService.getConfigurationById(1);
		Boolean newPublishedValue = !configuration.getPublished();
		Integer initialRevision = configuration.getRevision();
		
		map.put("uuid", configuration.getUuid());
		map.put("published", newPublishedValue);
		
		List<ConfigurationRevision> initilList = ddeService.getConfigurationRevisionsForConfiguration(configuration);
		
		configuration = ddeService.updateConfigurationFromMap(map);
		
		List<ConfigurationRevision> afterList = ddeService.getConfigurationRevisionsForConfiguration(configuration);
		
		assertEquals("Published field should be updated", newPublishedValue, configuration.getPublished());
		assertEquals("revision value should not change", initialRevision, configuration.getRevision());
		assertEquals("the number of revision should not change", initilList.size(), afterList.size());
	}
	
	@Test
	public void updateConfigurationFromMap_shouldUpateConfigurationRevisingIfFrequencyIsChanged() {
		Map<String, Object> map = new HashMap<String, Object>();
		Configuration configuration = ddeService.getConfigurationById(1);
		Float newFreqency = configuration.getFrequency() + 0.15f;
		Integer initialRevision = configuration.getRevision();
		
		map.put("uuid", configuration.getUuid());
		map.put("frequency", newFreqency);
		
		List<ConfigurationRevision> initilList = ddeService.getConfigurationRevisionsForConfiguration(configuration);
		
		configuration = ddeService.updateConfigurationFromMap(map);
		
		List<ConfigurationRevision> afterList = ddeService.getConfigurationRevisionsForConfiguration(configuration);
		
		assertEquals("Frequency field should be updated", newFreqency, configuration.getFrequency());
		assertEquals("revision should be updated", Integer.valueOf(initialRevision + 1), configuration.getRevision());
		assertEquals("the number of revision should increase by 1", initilList.size() + 1, afterList.size());
		
	}
}
