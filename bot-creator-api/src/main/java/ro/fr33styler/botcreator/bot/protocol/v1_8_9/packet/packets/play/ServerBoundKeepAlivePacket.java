package ro.fr33styler.botcreator.bot.protocol.v1_8_9.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ServerBoundKeepAlivePacket implements Packet {

    private final int keepAliveId;

    public ServerBoundKeepAlivePacket(int keepAliveId) {
        this.keepAliveId = keepAliveId;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, keepAliveId);
    }

}
