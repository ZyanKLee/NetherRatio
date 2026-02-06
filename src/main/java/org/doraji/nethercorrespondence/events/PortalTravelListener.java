package org.doraji.nethercorrespondence.events;

import org.doraji.nethercorrespondence.Nethercorrespondence;
import org.doraji.nethercorrespondence.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalTravelListener implements Listener {

    private final Nethercorrespondence plugin;
    private final ConfigManager cm;

    public PortalTravelListener(Nethercorrespondence plugin) {
        this.plugin = plugin;
        this.cm = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }

        Location newTo = calculatePortalDestination(event.getFrom());
        if (newTo != null) {
            event.setTo(newTo);
        } else {
            // World mapping not found, let vanilla behavior handle it or cancel if preferred
            // Currently allows vanilla portal mechanics to take over
            plugin.getLogger().fine("Portal destination could not be calculated, using vanilla behavior");
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        Location newTo = calculatePortalDestination(event.getFrom());
        if (newTo != null) {
            event.setTo(newTo);
        } else {
            // World mapping not found, let vanilla behavior handle it
            plugin.getLogger().fine("Entity portal destination could not be calculated, using vanilla behavior");
        }
    }

    private Location calculatePortalDestination(Location from) {
        double scale = cm.getDouble(ConfigManager.RATIO_VALUE);
        World fromWorld = from.getWorld();
        if (fromWorld == null) {
            plugin.getLogger().warning("Cannot calculate portal destination: source world is null");
            return null;
        }

        World toWorld;
        double newX;
        double newZ;

        if (fromWorld.getEnvironment() == World.Environment.NORMAL) {
            // When traveling from Overworld to Nether, divide by ratio
            // Example: 8:1 ratio means 800 in overworld = 100 in nether
            toWorld = cm.getLinkedNetherWorld(fromWorld.getName());
            if (toWorld == null) {
                // Log warning when world is not found
                plugin.getLogger().warning(
                    plugin.getMessagesManager().getMessage("config.world-not-found-overworld", "world", fromWorld.getName())
                );
                return null;
            }
            newX = from.getX() / scale;
            newZ = from.getZ() / scale;
        } else if (fromWorld.getEnvironment() == World.Environment.NETHER) {
            // When traveling from Nether to Overworld, multiply by ratio
            toWorld = cm.getLinkedOverworld(fromWorld.getName());
            if (toWorld == null) {
                // Log warning when world is not found
                plugin.getLogger().warning(
                    plugin.getMessagesManager().getMessage("config.world-not-found-nether", "world", fromWorld.getName())
                );
                return null;
            }
            newX = from.getX() * scale;
            newZ = from.getZ() * scale;
        } else {
            // End or other dimensions - no portal conversion
            return null;
        }

        return new Location(toWorld, newX, from.getY(), newZ, from.getYaw(), from.getPitch());
    }
}
