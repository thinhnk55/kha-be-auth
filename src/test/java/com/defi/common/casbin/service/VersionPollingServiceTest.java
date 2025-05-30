package com.defi.common.casbin.service;

import com.defi.common.casbin.config.CasbinProperties;
import org.casbin.jcasbin.main.Enforcer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for VersionPollingService.
 */
@ExtendWith(MockitoExtension.class)
class VersionPollingServiceTest {

    @Mock
    private CasbinProperties casbinProperties;

    @Mock
    private CasbinProperties.PollingConfig pollingConfig;

    @Mock
    private PolicyLoader policyLoader;

    @Mock
    private Enforcer enforcer;

    @Mock
    private DatabaseVersionChecker databaseVersionChecker;

    @Mock
    private ApiVersionChecker apiVersionChecker;

    private VersionPollingService versionPollingService;

    @BeforeEach
    void setUp() {
        when(casbinProperties.getPolling()).thenReturn(pollingConfig);

        versionPollingService = new VersionPollingService(
                casbinProperties,
                policyLoader,
                enforcer,
                databaseVersionChecker,
                apiVersionChecker);
    }

    @Test
    void initialize_whenPollingNotConfigured_shouldDisablePolling() {
        // Given
        when(pollingConfig.isValidForPolling()).thenReturn(false);

        // When
        versionPollingService.initialize();

        // Then
        assertFalse(versionPollingService.isPollingEnabled());
    }

    @Test
    void initialize_whenPollingConfiguredButDurationTooShort_shouldDisablePolling() {
        // Given
        when(pollingConfig.isValidForPolling()).thenReturn(true);
        when(pollingConfig.getDuration()).thenReturn(Duration.ofSeconds(30)); // Less than 1 minute
        when(casbinProperties.getPolicySource()).thenReturn("database:SELECT * FROM policy_rules");

        // When
        versionPollingService.initialize();

        // Then
        assertFalse(versionPollingService.isPollingEnabled());
    }

    @Test
    void initialize_whenResourcePolicySource_shouldDisablePolling() {
        // Given
        when(pollingConfig.isValidForPolling()).thenReturn(true);
        when(pollingConfig.getDuration()).thenReturn(Duration.ofMinutes(5));
        when(casbinProperties.getPolicySource()).thenReturn("resource:policy.csv");

        // When
        versionPollingService.initialize();

        // Then
        assertFalse(versionPollingService.isPollingEnabled());
    }

    @Test
    void initialize_whenDatabasePolicySourceAndValidConfig_shouldEnablePolling() {
        // Given
        when(pollingConfig.isValidForPolling()).thenReturn(true);
        when(pollingConfig.getDuration()).thenReturn(Duration.ofMinutes(5));
        when(casbinProperties.getPolicySource()).thenReturn("database:SELECT * FROM policy_rules");
        when(databaseVersionChecker.isAvailable()).thenReturn(true);

        // When
        versionPollingService.initialize();

        // Then
        assertTrue(versionPollingService.isPollingEnabled());
    }

    @Test
    void checkVersionAndReload_whenVersionChanged_shouldReloadPolicies() throws Exception {
        // Given
        setupValidPollingConfiguration();
        when(pollingConfig.getVersionCode()).thenReturn("policy_version");
        when(databaseVersionChecker.getCurrentVersion("policy_version"))
                .thenReturn(Optional.of(2L)); // Different from initial 0

        versionPollingService.initialize();
        versionPollingService.loadInitialVersion(); // Set initial version to 0

        // When
        versionPollingService.checkVersionAndReload();

        // Then
        verify(policyLoader).loadPolicies(enforcer);
        assertEquals(2L, versionPollingService.getCachedVersion());
    }

    @Test
    void checkVersionAndReload_whenVersionUnchanged_shouldNotReloadPolicies() throws Exception {
        // Given
        setupValidPollingConfiguration();
        when(pollingConfig.getVersionCode()).thenReturn("policy_version");
        when(databaseVersionChecker.getCurrentVersion("policy_version"))
                .thenReturn(Optional.of(1L));

        versionPollingService.initialize();

        // Simulate initial version load
        versionPollingService.loadInitialVersion();
        versionPollingService.checkVersionAndReload(); // First call sets version to 1

        // When - second call with same version
        versionPollingService.checkVersionAndReload();

        // Then - policy loader should only be called once (from first call)
        verify(policyLoader, times(1)).loadPolicies(enforcer);
    }

    private void setupValidPollingConfiguration() {
        when(pollingConfig.isValidForPolling()).thenReturn(true);
        when(pollingConfig.getDuration()).thenReturn(Duration.ofMinutes(5));
        when(casbinProperties.getPolicySource()).thenReturn("database:SELECT * FROM policy_rules");
        when(databaseVersionChecker.isAvailable()).thenReturn(true);
    }
}