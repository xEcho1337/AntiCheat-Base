package net.echo.anticheatbase.player.model;

import com.github.retrooper.packetevents.protocol.player.User;
import net.echo.anticheatbase.check.CheckManager;
import net.echo.anticheatbase.check.impl.module.LatencyModule;
import net.echo.anticheatbase.check.impl.module.PositionModule;

import java.util.UUID;

public class WrappedPlayer {

    private final LatencyModule latencyModule = new LatencyModule(this);
    private final PositionModule positionModule = new PositionModule(this);
    private final CheckManager checkManager = new CheckManager(this);
    private final User packetUser;

    public WrappedPlayer(User user) {
        this.packetUser = user;
    }

    public User getPacketUser() {
        return packetUser;
    }

    public UUID getUniqueId() {
        return packetUser.getUUID();
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public LatencyModule getLatencyModule() {
        return latencyModule;
    }

    public PositionModule getPositionModule() {
        return positionModule;
    }
}
