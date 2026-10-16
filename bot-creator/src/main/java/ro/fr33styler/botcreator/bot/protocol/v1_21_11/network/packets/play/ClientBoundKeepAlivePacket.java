package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundKeepAlivePacket implements Packet {

    private final long keepAliveId;

    public ClientBoundKeepAlivePacket(ByteBuf in) {
        keepAliveId = in.readLong();
    }

    @Override
    public int getId() {
        return 0x2B;
    }

    public long getKeepAliveId() {
        return keepAliveId;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeLong(keepAliveId);
    }

}
