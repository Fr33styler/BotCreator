package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundSetCompressPacketPacket implements Packet {

    private final int maximumPacketSize;

    public ClientBoundSetCompressPacketPacket(ByteBuf in) {
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
