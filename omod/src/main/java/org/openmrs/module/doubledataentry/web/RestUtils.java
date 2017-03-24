package org.openmrs.module.doubledataentry.web;

import org.openmrs.module.htmlformentry.HtmlForm;

import java.util.LinkedHashMap;

/**
 * Created by mhawilamhawila on 3/21/17.
 */
public class RestUtils {
	
	public static LinkedHashMap<String, Object> convertHtmlFormToMap(HtmlForm htmlForm) {
		LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
		
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
}
