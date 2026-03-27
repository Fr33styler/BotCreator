package ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundKeepAlivePacket implements Packet {

    private final int keepAliveId;

    public ClientBoundKeepAlivePacket(ByteBuf in) {
        keepAliveId = ByteBufUtil.readVarInt(in);
    }

    @Override
    public int getId() {
        return 0x00;
    }

    public int getKeepAliveId() {
        return keepAliveId;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, keepAliveId);
    }

}
