package org.openmrs.module.doubledataentry.web.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by Willa Mhawila on 3/10/17.
 */
public class HtmlFormInterceptorTest extends BaseModuleWebContextSensitiveTest {
	
	//    @Autowired private WebApplicationContext wac;
	//    @Autowired private MockHttpServletRequest request;
	//
	private MockMvc mockedMvc;
	
	@Before
	public void setup() {
		//        this.mockedMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void shouldInterceptRequest() throws Exception {
		//        MockHttpServletRequestBuilder request = get("/module/htmlformentry/htmlFormEntry", null)
		//                                                .accept(MediaType.ALL);
		//        this.mockedMvc.perform(request).andExpect(status().is(302));
	}
}
