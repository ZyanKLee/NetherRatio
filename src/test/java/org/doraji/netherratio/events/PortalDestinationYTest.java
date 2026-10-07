package org.doraji.netherratio.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.doraji.netherratio.PluginTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests for issue #45: the destination Y must stay within the destination world's usable height.
 */
class PortalDestinationYTest extends PluginTestBase {

    private Player player;

    @BeforeEach
    void setUpWorlds() {
        nether.setEnvironment(World.Environment.NETHER);
        player = server.addPlayer();
    }

    private Location travel(Location from, Location vanillaTo) {
        PlayerPortalEvent event = new PlayerPortalEvent(player, from, vanillaTo,
                PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
        server.getPluginManager().callEvent(event);
        return event.getTo();
    }

    private int netherCeiling() {
        return Math.min(nether.getMaxHeight(), nether.getMinHeight() + nether.getLogicalHeight()) - 1;
    }

    @Test
    void overworldYAboveNetherCeilingIsClamped() {
        Location to = travel(new Location(overworld, 800, 200, 800), new Location(nether, 0, 64, 0));

        assertSame(nether, to.getWorld());
        assertTrue(netherCeiling() < 200, "test setup: nether ceiling must be below the source Y");
        assertEquals(netherCeiling(), to.getY(), 0.0001);
    }

    @Test
    void yBelowDestinationMinimumIsClamped() {
        double belowMin = nether.getMinHeight() - 50;

        Location to = travel(new Location(overworld, 800, belowMin, 800), new Location(nether, 0, 64, 0));

        assertEquals(nether.getMinHeight(), to.getY(), 0.0001);
    }

    @Test
    void yWithinRangeIsUnchanged() {
        Location to = travel(new Location(overworld, 800, 70, 800), new Location(nether, 0, 64, 0));

        assertEquals(70, to.getY(), 0.0001);
    }

    @Test
    void netherToOverworldKeepsY() {
        Location to = travel(new Location(nether, 100, 40, 100), new Location(overworld, 0, 64, 0));

        assertSame(overworld, to.getWorld());
        assertEquals(800, to.getX(), 0.0001);
        assertEquals(40, to.getY(), 0.0001);
    }
}
