package org.xdi.oxd.spring.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.xdi.oxd.spring.OxdSpringApplication;

import javax.inject.Inject;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OxdSpringApplication.class)
@WebAppConfiguration
public class ApplicationControllerTest {

    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void user() throws Exception {
        mockMvc.perform(get("/user")).andExpect(view().name("fragments/user"))
                .andExpect(model().attribute("oxdId", anything())).andExpect(model().attribute("isLoggedIn", false))
                .andExpect(model().attribute("authorizationUrl", anything()))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(model().attribute("logoutUrl", nullValue()));
    }

    @Test
    public void home() throws Exception {
        mockMvc.perform(get("/home")).andExpect(view().name("fragments/home"))
                .andExpect(model().attribute("oxdId", anything())).andExpect(model().attribute("isLoggedIn", false))
                .andExpect(model().attribute("authorizationUrl", anything()))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(model().attribute("logoutUrl", nullValue()));
    }

    @Test
    public void loginError() throws Exception {
        mockMvc.perform(get("/error")).andExpect(view().name("fragments/error"))
                .andExpect(model().attribute("oxdId", anything())).andExpect(model().attribute("isLoggedIn", false))
                .andExpect(model().attribute("authorizationUrl", anything()))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(model().attribute("logoutUrl", nullValue()));
    }

}
