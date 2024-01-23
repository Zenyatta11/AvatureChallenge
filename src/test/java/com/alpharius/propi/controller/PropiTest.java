package com.alpharius.propi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@WebMvcTest(Propi.class)
class PropiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void hellothere() throws Exception {
        final MvcResult result = mvc.perform(get("/hellothere"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String responseString = result.getResponse().getContentAsString();
        assertEquals("General Kenobi", responseString, "Response should match.");
    }
}