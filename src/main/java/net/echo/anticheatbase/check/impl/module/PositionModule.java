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

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public void setLastZ(double lastZ) {
        this.lastZ = lastZ;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public boolean isClaimsOnGround() {
        return claimsOnGround;
    }

    public void setClaimsOnGround(boolean claimsOnGround) {
        this.claimsOnGround = claimsOnGround;
    }

    public boolean isLastClaimsOnGround() {
        return lastClaimsOnGround;
    }

    public void setLastClaimsOnGround(boolean lastClaimsOnGround) {
        this.lastClaimsOnGround = lastClaimsOnGround;
    }
}
