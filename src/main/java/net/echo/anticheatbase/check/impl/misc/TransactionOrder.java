package net.echo.anticheatbase.check.impl.misc;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.check.model.info.Category;
import net.echo.anticheatbase.check.model.info.CheckInfo;
import net.echo.anticheatbase.player.model.WrappedPlayer;

import java.util.Queue;

@CheckInfo(name = "Transaction Order", category = Category.MISC)
public class TransactionOrder extends AbstractCheck {

    public TransactionOrder(WrappedPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Queue<Integer> queue = player.getLatencyModule().getSentTransactions();

        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation(event);

            if (queue.isEmpty() || !queue.contains((int) wrapper.getActionId())) return;

            int expected = queue.poll();

            if (wrapper.getActionId() != expected) {
                addViolation("Transaction Order: " + expected + " != " + wrapper.getActionId());
            }
        }

        if (event.getPacketType() == PacketType.Play.Client.PONG) {
            WrapperPlayClientPong wrapper = new WrapperPlayClientPong(event);

            if (queue.isEmpty() || !queue.contains(wrapper.getId())) return;

            int expected = queue.poll();

            if (wrapper.getId() != expected) {
                addViolation("Transaction Order: " + expected + " != " + wrapper.getId());
            }
        }
    }
}
