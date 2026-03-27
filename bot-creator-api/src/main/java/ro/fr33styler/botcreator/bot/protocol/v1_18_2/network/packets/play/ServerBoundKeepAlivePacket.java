package ro.fr33styler.botcreator.bot.protocol.v1_18_2.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundKeepAlivePacket implements Packet {

    private final long keepAliveId;

    public ServerBoundKeepAlivePacket(long keepAliveId) {
        this.keepAliveId = keepAliveId;
    }

    @Override
    public int getId() {
        return 0x0F;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeLong(keepAliveId);
    }

}
