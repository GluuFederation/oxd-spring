package org.xdi.oxd.spring.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.xdi.oxd.spring.OxdSpringApplication;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OxdSpringApplication.class)
@WebAppConfiguration
public class GluuControllerTest {

    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void gluuRedirect() throws Exception {
        mockMvc.perform(get("/gluu/callback").param("session_state", "session_state").param("scope", "scope")
                .param("state", "state").param("code", "code")).andExpect(view().name("redirect:/error"));
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(get("/gluu/logout")).andExpect(view().name("redirect:/home"));
    }
}
