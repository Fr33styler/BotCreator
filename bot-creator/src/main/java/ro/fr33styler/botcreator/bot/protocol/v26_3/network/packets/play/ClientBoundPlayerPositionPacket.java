package ro.fr33styler.botcreator.bot.protocol.v26_3.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundPlayerPositionPacket implements Packet {

    private final int teleportId;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public ClientBoundPlayerPositionPacket(ByteBuf in) {
        teleportId = ByteBufUtil.readVarInt(in);
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        in.readDouble();
        in.readDouble();
        in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
    }

    @Override
    public int getId() {
        return 0x49;
    }

    public int getTeleportId() {
        return teleportId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @Override
    public void encode(ByteBuf out) {}

}
