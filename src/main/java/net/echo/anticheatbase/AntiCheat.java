package net.echo.anticheatbase;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.echo.anticheatbase.listeners.PacketListener;
import net.echo.anticheatbase.player.PlayerManager;
import net.echo.anticheatbase.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiCheat extends JavaPlugin {

    private final PlayerManager playerManager = new PlayerManager();

    @Override
    public void onEnable() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        PacketEvents.getAPI().getSettings().checkForUpdates(true).bStats(true);
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListener(this));

        TaskManager taskManager = new TaskManager(this);
        Bukkit.getScheduler().runTaskTimer(this, taskManager::onTick, 1, 1);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
