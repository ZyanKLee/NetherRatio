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

    private final ConfigManager cm;

    public PortalTravelListener(Nethercorrespondence plugin) {
        this.cm = plugin.getSettingsManager();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        // 지옥문에 의한 이동이 아니면 무시
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }

        Location newTo = calculatePortalDestination(event.getFrom());
        if (newTo != null) {
            event.setTo(newTo);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        // PlayerPortalEvent가 플레이어를 처리하므로, 여기서는 플레이어가 아닌 엔티티만 다룹니다.
        Location newTo = calculatePortalDestination(event.getFrom());
        if (newTo != null) {
            event.setTo(newTo);
        }
    }

    /**
     * 포탈을 통과한 후의 목적지 좌표를 계산합니다.
     * @param from 출발지 Location
     * @return 계산된 목적지 Location, 계산이 불가능하면 null
     */
    private Location calculatePortalDestination(Location from) {
        double scale = cm.getDouble(ConfigManager.RATIO_VALUE);
        World fromWorld = from.getWorld();
        if (fromWorld == null) return null;

        World toWorld;
        double newX;
        double newZ;

        if (fromWorld.getEnvironment() == World.Environment.NORMAL) {
            toWorld = Bukkit.getWorld(fromWorld.getName() + "_nether");
            if (toWorld == null) return null;
            newX = from.getX() / scale;
            newZ = from.getZ() / scale;
        } else if (fromWorld.getEnvironment() == World.Environment.NETHER) {
            String overworldName = fromWorld.getName().replace("_nether", "");
            toWorld = Bukkit.getWorld(overworldName);
            if (toWorld == null) return null;
            newX = from.getX() * scale;
            newZ = from.getZ() * scale;
        } else {
            return null; // 다른 차원 간의 이동은 처리하지 않음
        }

        return new Location(toWorld, newX, from.getY(), newZ, from.getYaw(), from.getPitch());
    }
}
