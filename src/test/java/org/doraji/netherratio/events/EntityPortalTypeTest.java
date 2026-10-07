package org.doraji.netherratio.events;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalEvent;
import org.doraji.netherratio.PluginTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.world.WorldMock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Regression tests for issue #43: entity end-portal travel must not be redirected to the Nether.
 */
class EntityPortalTypeTest extends PluginTestBase {

    private WorldMock end;
    private Player entity;

    @BeforeEach
    void setUpWorlds() {
        nether.setEnvironment(World.Environment.NETHER);
        end = server.addSimpleWorld("world_the_end");
        end.setEnvironment(World.Environment.THE_END);
        entity = server.addPlayer();
    }

    private EntityPortalEvent fire(Location from, Location to, PortalType type) {
        EntityPortalEvent event = new EntityPortalEvent(entity, from, to, 128, true, 16, type);
        server.getPluginManager().callEvent(event);
        return event;
    }

    @Test
    void endPortalDestinationIsNotModified() {
        Location from = new Location(overworld, 800, 64, 800);
        Location endDestination = new Location(end, 100, 50, 0);

        EntityPortalEvent event = fire(from, endDestination, PortalType.ENDER);

        assertSame(end, event.getTo().getWorld());
        assertEquals(100, event.getTo().getX(), 0.0001);
        assertEquals(0, event.getTo().getZ(), 0.0001);
    }

    @Test
    void endGatewayDestinationIsNotModified() {
        Location from = new Location(overworld, 800, 64, 800);
        Location gatewayDestination = new Location(end, 1000, 70, 1000);

        EntityPortalEvent event = fire(from, gatewayDestination, PortalType.END_GATEWAY);

        assertSame(end, event.getTo().getWorld());
        assertEquals(1000, event.getTo().getX(), 0.0001);
    }

    @Test
    void netherPortalDestinationIsScaled() {
        Location from = new Location(overworld, 800, 64, -1600);
        Location vanillaDestination = new Location(nether, 0, 64, 0);

        EntityPortalEvent event = fire(from, vanillaDestination, PortalType.NETHER);

        assertSame(nether, event.getTo().getWorld());
        assertEquals(100, event.getTo().getX(), 0.0001);
        assertEquals(-200, event.getTo().getZ(), 0.0001);
    }
}
