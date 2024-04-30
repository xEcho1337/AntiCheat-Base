package net.echo.anticheatbase.listeners;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import net.echo.anticheatbase.AntiCheat;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.player.PlayerManager;
import net.echo.anticheatbase.player.model.WrappedPlayer;

public class PacketListener extends PacketListenerAbstract {

    private final AntiCheat antiCheat;

    public PacketListener(AntiCheat antiCheat) {
        super(PacketListenerPriority.HIGH);
        this.antiCheat = antiCheat;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrappedPlayer player = antiCheat.getPlayerManager().getPlayer(event.getUser().getUUID());

        if (player == null) return;

        for (AbstractCheck check : player.getCheckManager().getAllChecks()) {
            check.onPacketReceive(event);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PlayerManager playerManager = antiCheat.getPlayerManager();

        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            antiCheat.getPlayerManager().addPlayer(new WrappedPlayer(event.getUser()));
        }

        WrappedPlayer player = playerManager.getPlayer(event.getUser().getUUID());

        if (player == null) return;

        for (AbstractCheck check : player.getCheckManager().getAllChecks()) {
            check.onPacketSend(event);
        }
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        antiCheat.getPlayerManager().removePlayer(event.getUser().getUUID());
    }
}
