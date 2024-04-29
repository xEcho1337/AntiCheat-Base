package net.echo.anticheatbase.check.model;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import net.echo.anticheatbase.check.model.info.CheckInfo;
import net.echo.anticheatbase.player.model.WrappedPlayer;

import java.util.concurrent.atomic.AtomicInteger;

public class AbstractCheck {

    protected final WrappedPlayer player;
    private final CheckInfo checkInfo;
    private final AtomicInteger flags = new AtomicInteger(0);

    public AbstractCheck(WrappedPlayer player) {
        this.player = player;
        this.checkInfo = this.getClass().getAnnotation(CheckInfo.class);
    }

    public void onPacketReceive(PacketReceiveEvent event) {
    }

    public void onPacketSend(PacketSendEvent event) {
    }

    public void addViolation(String verbose) {
        flags.getAndIncrement();
        System.out.println(player.getPacketUser().getName() + " violated " + checkInfo.name() + " -> " + verbose);
    }

    public CheckInfo getCheckInfo() {
        return checkInfo;
    }

    public WrappedPlayer getPlayer() {
        return player;
    }
}
