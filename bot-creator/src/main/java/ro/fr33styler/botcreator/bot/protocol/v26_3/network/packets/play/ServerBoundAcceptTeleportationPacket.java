package ro.fr33styler.botcreator.bot.protocol.v26_3.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundAcceptTeleportationPacket implements Packet {

    private final int teleportId;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public ServerBoundAcceptTeleportationPacket(ClientBoundPlayerPositionPacket packet) {
        this.teleportId = packet.getTeleportId();
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, teleportId);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
    }

}
