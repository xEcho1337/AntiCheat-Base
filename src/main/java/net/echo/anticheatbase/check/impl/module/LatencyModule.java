package net.echo.anticheatbase.check.impl.module;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.player.model.WrappedPlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LatencyModule extends AbstractCheck {

    private final AtomicInteger currentTransaction = new AtomicInteger(0);
    private final AtomicInteger lastReceivedTransaction = new AtomicInteger(0);
    private final AtomicInteger lastSentTransaction = new AtomicInteger(0);
    private final Queue<Integer> sentTransactions = new LinkedList<>();

    public LatencyModule(WrappedPlayer player) {
        super(player);
    }

    public void sendTransaction() {
        short transaction = (short) currentTransaction.decrementAndGet();

        WrapperPlayServerWindowConfirmation wrapper = new WrapperPlayServerWindowConfirmation(0, transaction, false);
        player.getPacketUser().sendPacket(wrapper);

        lastSentTransaction.set(transaction);
        sentTransactions.add((int) transaction);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation(event);

            if (!sentTransactions.contains((int) wrapper.getActionId())) return;

            lastReceivedTransaction.set(wrapper.getActionId());
            sentTransactions.remove((int) wrapper.getActionId());
        }

        if (event.getPacketType() == PacketType.Play.Client.PONG) {
            WrapperPlayClientPong wrapper = new WrapperPlayClientPong(event);

            if (!sentTransactions.contains(wrapper.getId())) return;

            lastReceivedTransaction.set(wrapper.getId());
            sentTransactions.remove(wrapper.getId());
        }
    }

    public AtomicInteger getCurrentTransaction() {
        return currentTransaction;
    }

    public AtomicInteger getLastReceivedTransaction() {
        return lastReceivedTransaction;
    }

    public AtomicInteger getLastSentTransaction() {
        return lastSentTransaction;
    }

    public Queue<Integer> getSentTransactions() {
        return sentTransactions;
    }
}
