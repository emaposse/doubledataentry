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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.Participant;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface DoubleDataEntryService extends OpenmrsService {
	
	@Authorized
	@Transactional
	public List<HtmlForm> searchHtmlFormsByName(String search);
	
	public List<Configuration> getAllConfigurations();
	
	public List<Participant> getAllParticipants();
	
	public List<HtmlForm> getAllHtmlFormsHavingConfigurations();
	
	public Configuration createConfigurationFromMap(Map<String, Object> configurationMap);
	
	public List<Configuration> createConfigurationsFromMaps(List<Map<String, Object>> configurationMaps);
}
