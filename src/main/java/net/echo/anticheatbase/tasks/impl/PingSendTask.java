package net.echo.anticheatbase.tasks.impl;

import net.echo.anticheatbase.AntiCheat;
import net.echo.anticheatbase.player.model.WrappedPlayer;
import net.echo.anticheatbase.tasks.model.AbstractTask;

public class PingSendTask extends AbstractTask {

    private final AntiCheat antiCheat;

    public PingSendTask(AntiCheat antiCheat) {
        this.antiCheat = antiCheat;
    }

    @Override
    public void run() {
        for (WrappedPlayer player : antiCheat.getPlayerManager().getPlayers().values()) {
            player.getLatencyModule().sendTransaction();
        }
    }
}
