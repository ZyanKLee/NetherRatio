package org.doraji.netherratio;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests for issue #44: getOverworldNames() must not expose the live backing set.
 */
class ConfigManagerOverworldNamesTest extends PluginTestBase {

    @Test
    void returnedSetIsNotAffectedByReload() {
        ConfigManager cm = plugin.getConfigManager();
        Set<String> before = cm.getOverworldNames();

        cm.setValue(ConfigManager.WORLD_PAIRS + ".creative", "creative_nether");
        cm.reload();

        assertEquals(Set.of("world"), before);
        assertTrue(cm.getOverworldNames().contains("creative"));
    }

    @Test
    void returnedSetCannotModifyConfiguration() {
        ConfigManager cm = plugin.getConfigManager();
        Set<String> names = cm.getOverworldNames();

        assertThrows(UnsupportedOperationException.class, () -> names.remove("world"));
        assertTrue(cm.getOverworldNames().contains("world"));
        assertFalse(cm.getOverworldNames().isEmpty());
    }
}
