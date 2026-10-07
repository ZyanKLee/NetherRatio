package org.doraji.netherratio;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for all MockBukkit integration tests.
 *
 * <p>Sets up a {@link ServerMock} with two pre-configured worlds (an overworld and a nether)
 * that match the default plugin configuration, so subclasses can focus on the behaviour
 * under test without repeating setup boilerplate.</p>
 */
public abstract class PluginTestBase {

    protected ServerMock server;
    protected NetherRatio plugin;
    protected WorldMock overworld;
    protected WorldMock nether;

    @BeforeEach
    void setUpServer() {
        server = MockBukkit.mock();

        overworld = server.addSimpleWorld("world");
        nether = server.addSimpleWorld("world_nether");

        plugin = MockBukkit.load(NetherRatio.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }
}
