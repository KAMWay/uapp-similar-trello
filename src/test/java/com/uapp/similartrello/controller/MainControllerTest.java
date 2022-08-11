package com.uapp.similartrello.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MainControllerTest extends IntegrationTest {

    @Test
    public void givenMainController_ShouldBeNotNull() {
        assertNotNull(context);
        assertNotNull(context.getBean("mainController"));
    }

    @Test
    void givenHomePageURI_ShouldBeTrue() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();

        assertTrue(actualResult.contains("Add new group"));
    }
}