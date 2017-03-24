package org.openmrs.module.doubledataentry.web;

import org.apache.commons.collections.map.LinkedMap;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.htmlformentry.HtmlForm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mhawilamhawila on 3/21/17.
 */
public class RestUtils {
	
	public static Map<String, Object> convertHtmlFormToMap(HtmlForm htmlForm) {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		
		ret.put("formId", htmlForm.getId());
		if (htmlForm.getForm() != null) {
			ret.put("name", htmlForm.getForm().getName());
			ret.put("description", htmlForm.getForm().getDescription());
			ret.put("version", htmlForm.getForm().getVersion());
			ret.put("published", htmlForm.getForm().getPublished());
		} else {
			ret.put("name", htmlForm.getName());
			ret.put("description", htmlForm.getDescription());
		}
		
		ret.put("retired", htmlForm.getRetired());
		ret.put("uuid", htmlForm.getUuid());
		
		return ret;
	}
	
	public static Map<String, Object> convertConfigurationToMap(Configuration configuration) {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		
		ret.put("id", configuration.getId());
		ret.put("htmlForm", convertHtmlFormToMap(configuration.getHtmlForm()));
		ret.put("frequency", configuration.getFrequency());
		ret.put("revision", configuration.getRevision());
		ret.put("published", configuration.getPublished());
		ret.put("retired", configuration.getRetired());
		ret.put("uuid", configuration.getUuid());
		
		return ret;
	}
}
