package com.defi.auth.permission.controller;

import com.defi.common.casbin.entity.PolicyRule;
import com.defi.common.casbin.service.DatabasePolicyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PolicyController.
 */
@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabasePolicyLoader databasePolicyLoader;

    private List<PolicyRule> mockPolicies;

    @BeforeEach
    void setUp() {
        mockPolicies = Arrays.asList(
                PolicyRule.builder()
                        .id(1L)
                        .roleId(1L)
                        .resourceCode("users")
                        .actionCode("read")
                        .build(),
                PolicyRule.builder()
                        .id(2L)
                        .roleId(1L)
                        .resourceCode("roles")
                        .actionCode("create")
                        .build());
    }

    @Test
    void getPolicies_withoutFilter_shouldReturnAllPolicies() throws Exception {
        // Given
        when(databasePolicyLoader.loadPolicyRulesFromDatabase(anyString(), eq(Collections.emptyList())))
                .thenReturn(mockPolicies);

        // When & Then
        mockMvc.perform(get("/api/v1/policies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].resourceCode").value("users"))
                .andExpect(jsonPath("$.data[1].resourceCode").value("roles"));
    }

    @Test
    void getPolicies_withSingleResource_shouldReturnFilteredPolicies() throws Exception {
        // Given
        List<PolicyRule> filteredPolicies = List.of(mockPolicies.get(0));
        when(databasePolicyLoader.loadPolicyRulesFromDatabase(anyString(), eq(List.of("users"))))
                .thenReturn(filteredPolicies);

        // When & Then
        mockMvc.perform(get("/api/v1/policies")
                .param("resources", "users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].resourceCode").value("users"));
    }

    @Test
    void getPolicies_withMultipleResources_shouldReturnFilteredPolicies() throws Exception {
        // Given
        when(databasePolicyLoader.loadPolicyRulesFromDatabase(anyString(), eq(Arrays.asList("users", "roles"))))
                .thenReturn(mockPolicies);

        // When & Then
        mockMvc.perform(get("/api/v1/policies")
                .param("resources", "users,roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void getPolicies_whenDatabaseError_shouldReturnInternalServerError() throws Exception {
        // Given
        when(databasePolicyLoader.loadPolicyRulesFromDatabase(anyString(), any()))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/v1/policies"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("Failed to fetch policies"));
    }
}