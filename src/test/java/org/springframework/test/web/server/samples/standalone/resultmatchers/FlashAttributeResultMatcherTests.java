/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.test.web.server.samples.standalone.resultmatchers;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.server.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Examples of expectations on flash attributes.
 *
 * @author Rossen Stoyanchev
 */
public class FlashAttributeResultMatcherTests {

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = standaloneSetup(new PersonController()).build();
	}

	@Test
	public void testExists() throws Exception {
		this.mockMvc.perform(post("/persons"))
            .andExpect(flash().attributeExists("one", "two", "three"));
	}

	@Test
	public void testCount() throws Exception {
		this.mockMvc.perform(post("/persons"))
            .andExpect(flash().attributeCount(3));
	}

	@Test
	public void testEqualTo() throws Exception {
		this.mockMvc.perform(post("/persons"))
            .andExpect(flash().attribute("one", "1"))
            .andExpect(flash().attribute("two", 2.222))
            .andExpect(flash().attribute("three", new URL("http://example.com")));

		// Hamcrest matchers...
		this.mockMvc.perform(post("/persons"))
	        .andExpect(flash().attribute("one", equalTo("1")))
	        .andExpect(flash().attribute("two", equalTo(2.222)))
	        .andExpect(flash().attribute("three", equalTo(new URL("http://example.com"))));
	}

	@Test
	public void testMatchers() throws Exception {
		this.mockMvc.perform(post("/persons"))
	        .andExpect(flash().attribute("one", containsString("1")))
	        .andExpect(flash().attribute("two", closeTo(2, 0.5)))
	        .andExpect(flash().attribute("three", notNullValue()));
	}


	@Controller
	private static class PersonController {

		@RequestMapping(value="/persons", method=RequestMethod.POST)
		public String save(RedirectAttributes redirectAttrs) throws Exception {
			redirectAttrs.addFlashAttribute("one", "1");
			redirectAttrs.addFlashAttribute("two", 2.222);
			redirectAttrs.addFlashAttribute("three", new URL("http://example.com"));
			return "redirect:/person/1";
		}
	}
}
