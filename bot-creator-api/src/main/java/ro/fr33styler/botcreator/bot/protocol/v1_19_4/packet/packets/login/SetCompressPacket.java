package ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class SetCompressPacket implements Packet {

    private final int maximumPacketSize;

    public SetCompressPacket(ByteBuf in) {
        maximumPacketSize = ByteBufUtil.readVarInt(in);
    }

    public int getMaximumPacketSize() {
        return maximumPacketSize;
    }

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, maximumPacketSize);
    }

}
