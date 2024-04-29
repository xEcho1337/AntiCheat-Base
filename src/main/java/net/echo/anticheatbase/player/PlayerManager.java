package net.echo.anticheatbase.player;

import net.echo.anticheatbase.player.model.WrappedPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final Map<UUID, WrappedPlayer> players = new ConcurrentHashMap<>();

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public void addPlayer(WrappedPlayer player) {
        players.put(player.getUniqueId(), player);
    }

    public Map<UUID, WrappedPlayer> getPlayers() {
        return players;
    }

    public WrappedPlayer getPlayer(UUID uuid) {
        if (uuid == null) return null;

        return players.get(uuid);
    }
}
