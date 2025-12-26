package com.medilabosolutions.frontservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that a GET request to /login returns the login page view.
     */
    @Test
    void testLoginPage_returnsLoginView() throws Exception {
        // Given
        // A GET request to /login

        // When
        mockMvc.perform(get("/login"))
                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("login-page"));
    }

}
