package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundPlayerPositionPacket implements Packet {

    private final int teleportId;

    public ClientBoundPlayerPositionPacket(ByteBuf in) {
        in.readDouble();
        in.readDouble();
        in.readDouble();

        in.readFloat();
        in.readFloat();

        in.readUnsignedByte();

        teleportId = ByteBufUtil.readVarInt(in);
    }

    @Override
    public int getId() {
        return 0x40;
    }

    public int getTeleportId() {
        return teleportId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
