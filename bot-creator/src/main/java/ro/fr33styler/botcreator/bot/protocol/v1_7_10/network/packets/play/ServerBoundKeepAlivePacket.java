package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

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
        out.writeInt(keepAliveId);
    }

}
