package org.openmrs.module.doubledataentry.web;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Form;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.web.RestUtils;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.util.OpenmrsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by Willa Mhawila on 3/21/17.
 */
public class RestUtilsTest {
	
	@Test
	public void convertedHtmlShouldHaveDefinedProperties() {
		HtmlForm htmlForm = new HtmlForm();
		String uuid = OpenmrsUtil.generateUid();
		htmlForm.setUuid(uuid);
		Form form = new Form();
		htmlForm.setForm(form);
		Map<String, Object> converted = RestUtils.convertHtmlFormToMap(htmlForm);
		
		assertTrue(converted.containsKey("formId"));
		assertTrue(converted.containsKey("uuid"));
		assertTrue(converted.containsKey("name"));
		assertTrue(converted.containsKey("description"));
		assertTrue(converted.containsKey("published"));
		assertTrue(converted.containsKey("retired"));
		assertTrue(converted.containsKey("version"));
	}
	
	@Test
	public void convertConfigurationToMap_shouldHaveDefinedProperties() {
		Configuration configuration = new Configuration();
		HtmlForm htmlForm = new HtmlForm();
		Form form = new Form();
		htmlForm.setForm(form);
		String uuid = OpenmrsUtil.generateUid();
		
		configuration.setId(1000);
		configuration.setHtmlForm(htmlForm);
		configuration.setUuid(uuid);
		
		Map<String, Object> converted = RestUtils.convertConfigurationToMap(configuration);
		assertTrue(converted.containsKey("id"));
		assertTrue(converted.containsKey("uuid"));
		assertTrue(converted.containsKey("htmlForm"));
		assertTrue(converted.containsKey("frequency"));
		assertTrue(converted.containsKey("revision"));
		assertTrue(converted.containsKey("published"));
		assertTrue(converted.containsKey("retired"));
	}
}
