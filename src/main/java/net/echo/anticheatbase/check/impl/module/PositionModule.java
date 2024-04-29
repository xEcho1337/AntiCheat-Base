package net.echo.anticheatbase.check.impl.module;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.player.model.WrappedPlayer;

public class PositionModule extends AbstractCheck {

    private double posX, posY, posZ;
    private double lastX, lastY, lastZ;
    private float yaw, lastYaw, pitch, lastPitch;
    private boolean claimsOnGround, lastClaimsOnGround;

    public PositionModule(WrappedPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            lastClaimsOnGround = claimsOnGround;
            claimsOnGround = wrapper.isOnGround();

            Location location = wrapper.getLocation();

            if (wrapper.hasPositionChanged()) {
                lastX = posX;
                lastY = posY;
                lastZ = posZ;

                posX = location.getX();
                posY = location.getY();
                posZ = location.getZ();
            }

            if (wrapper.hasRotationChanged()) {
                lastYaw = yaw;
                lastPitch = pitch;

                yaw = location.getYaw();
                pitch = location.getPitch();
            }
        }
    }
}
