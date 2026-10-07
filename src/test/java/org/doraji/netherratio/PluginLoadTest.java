package org.doraji.netherratio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke tests: verifies the plugin enables cleanly and exposes its
 * managers, so regressions in startup wiring are caught immediately.
 */
class PluginLoadTest extends PluginTestBase {

    @Test
    void pluginEnablesSuccessfully() {
        assertTrue(plugin.isEnabled());
    }

    @Test
    void configManagerIsInitialised() {
        assertNotNull(plugin.getConfigManager());
    }

    @Test
    void messagesManagerIsInitialised() {
        assertNotNull(plugin.getMessagesManager());
    }

    @Test
    void defaultRatioIsEight() {
        assertEquals(8.0, plugin.getConfigManager().getDefaultRatio(), 0.0001);
    }

    @Test
    void defaultWorldPairLoaded() {
        assertTrue(plugin.getConfigManager().getOverworldNames().contains("world"),
                "Default world pair 'world' should be loaded");
    }
}
