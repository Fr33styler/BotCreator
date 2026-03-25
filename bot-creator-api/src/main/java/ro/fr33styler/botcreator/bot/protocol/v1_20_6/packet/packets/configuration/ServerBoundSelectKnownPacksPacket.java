package ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ServerBoundSelectKnownPacksPacket implements Packet {

    @Override
    public int getId() {
        return 0x07;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, 1);
        ByteBufUtil.writeString(out, "minecraft");
        ByteBufUtil.writeString(out, "core");
        ByteBufUtil.writeString(out, "1.21.11");
    }

}
