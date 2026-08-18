package com.example.customerservice.profile;

import com.example.customerservice.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerProfileController.class)
@Import(SecurityConfig.class)
class CustomerProfileSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean CustomerProfileService service;

    @Test
    void rejectsAnonymousProfileRequests() throws Exception {
        mockMvc.perform(get("/api/customers/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void permitsJwtAuthenticatedProfileRequests() throws Exception {
        UUID subject = UUID.randomUUID();
        when(service.getOrCreate(eq(subject), any(), any(), any(), isNull()))
                .thenReturn(CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null));
        mockMvc.perform(get("/api/customers/me").with(jwt().jwt(jwt -> jwt.subject(subject.toString()))))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsInvalidProfileUpdates() throws Exception {
        mockMvc.perform(put("/api/customers/me")
                        .with(jwt())
                        .contentType("application/json")
                        .content("{\"firstName\":\"\",\"lastName\":\"Mashongwa\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }
}
