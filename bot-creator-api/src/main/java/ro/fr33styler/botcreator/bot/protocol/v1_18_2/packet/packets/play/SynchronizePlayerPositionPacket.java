package ro.fr33styler.botcreator.bot.protocol.v1_18_2.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class SynchronizePlayerPositionPacket implements Packet {

    private final double x;
    private final double y;
    private final double z;

    private final float yaw;
    private final float pitch;

    private final int flags;

    private final int teleportId;
    private final boolean dismountVehicle;

    public SynchronizePlayerPositionPacket(ByteBuf in) {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();

        yaw = in.readFloat();
        pitch = in.readFloat();

        flags = in.readUnsignedByte();

        teleportId = ByteBufUtil.readVarInt(in);

        this.dismountVehicle = in.readBoolean();
    }

    @Override
    public int getId() {
        return 0x38;
    }

    public int getTeleportId() {
        return teleportId;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);

        out.writeFloat(yaw);
        out.writeFloat(pitch);

        out.writeByte(flags);

        ByteBufUtil.writeVarInt(out, teleportId);
        out.writeBoolean(dismountVehicle);
    }

}
