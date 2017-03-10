package org.openmrs.module.doubledataentry.web.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mhawilamhawila on 3/10/17.
 */
public class HtmlFormInterceptor extends HandlerInterceptorAdapter {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		return true;
	}
}
