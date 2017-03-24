package org.openmrs.module.doubledataentry.web;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Form;
import org.openmrs.module.doubledataentry.web.RestUtils;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.util.OpenmrsUtil;

import java.util.LinkedHashMap;

/**
 * Created by Willa Mhawila on 3/21/17.
 */
public class RestUtilsTest {
	
	@Test
	public void convertedHtmlShouldHaveDefinedProperty() {
		HtmlForm htmlForm = new HtmlForm();
		String uuid = OpenmrsUtil.generateUid();
		htmlForm.setUuid(uuid);
		Form form = new Form();
		htmlForm.setForm(form);
		LinkedHashMap<String, Object> converted = RestUtils.convertHtmlFormToMap(htmlForm);
		
		Assert.assertTrue(converted.containsKey("formId"));
		Assert.assertTrue(converted.containsKey("uuid"));
		Assert.assertTrue(converted.containsKey("name"));
		Assert.assertTrue(converted.containsKey("description"));
		Assert.assertTrue(converted.containsKey("published"));
		Assert.assertTrue(converted.containsKey("retired"));
		Assert.assertTrue(converted.containsKey("version"));
	}
}
