package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class SynchronizePlayerPositionPacket implements Packet {

    private final int teleportId;
    private final double x;
    private final double y;
    private final double z;

    private final double motX;
    private final double motY;
    private final double motZ;

    private final float yaw;
    private final float pitch;

    private final int flags;

    public SynchronizePlayerPositionPacket(ByteBuf in) {
        teleportId = ByteBufUtil.readVarInt(in);

        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();

        motX = in.readDouble();
        motY = in.readDouble();
        motZ = in.readDouble();

        yaw = in.readFloat();
        pitch = in.readFloat();

        flags = in.readInt();
    }

    @Override
    public int getId() {
        return 0x46;
    }

    public int getTeleportId() {
        return teleportId;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, teleportId);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);

        out.writeDouble(motX);
        out.writeDouble(motY);
        out.writeDouble(motZ);

        out.writeFloat(yaw);
        out.writeFloat(pitch);

        out.writeInt(flags);
    }

}
