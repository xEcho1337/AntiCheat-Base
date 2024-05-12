package net.echo.anticheatbase.check.impl.module;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.player.model.WrappedPlayer;
import net.echo.anticheatbase.player.model.data.TransactionData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class LatencyModule extends AbstractCheck {

    private final Map<Integer, TransactionCallbacks> pendingCallbacks = new ConcurrentHashMap<>();
    private final Queue<TransactionData> sentTransactions = new LinkedList<>();
    private final AtomicInteger lastReceivedTransaction = new AtomicInteger(0);
    private final AtomicInteger lastSentTransaction = new AtomicInteger(1);
    private long lastTransactionTimestamp;

    public LatencyModule(WrappedPlayer player) {
        super(player);
    }

    public void addCallback(int transaction, Consumer<Integer> callback) {
        pendingCallbacks.computeIfAbsent(transaction, key -> new TransactionCallbacks()).add(callback);
    }

    public void addCurrentCallback(Consumer<Integer> callback) {
        if (callback == null) return;
        addCallback(lastSentTransaction.get(), callback);
    }

    public void addNextCallback(Consumer<Integer> callback) {
        addCallback(lastSentTransaction.get() + 1, callback);
    }

    public void sendTransaction(Consumer<Integer> callback) {
        short transaction = (short) lastSentTransaction.decrementAndGet();

        WrapperPlayServerWindowConfirmation wrapper = new WrapperPlayServerWindowConfirmation(0, transaction, false);
        player.getPacketUser().writePacket(wrapper);

        TransactionData data = new TransactionData(transaction, System.nanoTime());
        sentTransactions.add(data);

        addCurrentCallback(callback);
        lastTransactionTimestamp = System.currentTimeMillis();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation(event);
            handleTransaction(wrapper.getActionId(), event);
        }

        if (event.getPacketType() == PacketType.Play.Client.PONG) {
            WrapperPlayClientPong wrapper = new WrapperPlayClientPong(event);
            handleTransaction(wrapper.getId(), event);
        }
    }

    public void handleTransaction(int transaction, PacketReceiveEvent event) {
        if (!contains(transaction)) return;

        lastReceivedTransaction.set(transaction);
        TransactionData current = sentTransactions.stream().filter(data -> data.getId() == transaction).findFirst().orElse(null);

        // If the player has skipped (or cancelled) transactions we still run the callbacks
        Iterator<Map.Entry<Integer, TransactionCallbacks>> iterator = pendingCallbacks.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, TransactionCallbacks> entry = iterator.next();

            if (entry.getKey() < transaction) continue;

            entry.getValue().forEach(callback -> callback.accept(transaction));
            iterator.remove();
        }

        // Remove from the queue after the reception
        event.getPostTasks().add(() -> sentTransactions.remove(current));
    }

    public Queue<TransactionData> getQueue() {
        return sentTransactions;
    }

    public AtomicInteger getLastReceivedTransaction() {
        return lastReceivedTransaction;
    }

    public AtomicInteger getLastSentTransaction() {
        return lastSentTransaction;
    }

    public boolean contains(int transaction) {
        return sentTransactions.stream().anyMatch(data -> data.getId() == transaction);
    }

    public long getLastTransactionTimestamp() {
        return lastTransactionTimestamp;
    }

    // Just for code clean up
    static class TransactionCallbacks extends ArrayList<Consumer<Integer>> { }
}
