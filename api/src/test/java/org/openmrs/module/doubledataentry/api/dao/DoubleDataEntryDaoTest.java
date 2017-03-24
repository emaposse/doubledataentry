/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.doubledataentry.api.dao;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class DoubleDataEntryDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	DoubleDataEntryDao dao;
	
	private static final String datasetFileName = "test-data.xml";
	
	@Before
	public void setup() throws Exception {
		executeDataSet(datasetFileName);
	}
	
	@Test
	public void searchHtmlForms_shouldSearchHtmlFormsNames() throws Exception {
		List<HtmlForm> forms = dao.searchHtmlForms("two");
		
		assertNotNull("forms should be null", forms);
		assertTrue(forms.size() > 0);
		assertTrue("The form with two in it should be in results", forms.get(0).getForm().getName().contains("two"));
	}
	
	@Test
	public void searchHtmlForms_shouldReturnEmptyListIfNothingFound() throws Exception {
		List<HtmlForm> forms = dao.searchHtmlForms("xyz  two dalkd  d79ld");
		
		assertTrue("The list should be empty", forms.size() == 0);
	}
	
	@Test
	public void getHtmlFormsUsedInConfiguration_ShouldReturnAllUsedForms() {
		List<HtmlForm> forms = dao.getHtmlFormsUsedInConfigurations();
		
		assertTrue(!forms.isEmpty());
		assertTrue(forms.size() == 2);
	}
}
