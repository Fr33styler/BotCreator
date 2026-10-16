package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundKeepAlivePacket implements Packet {

    private final int keepAliveId;

    public ClientBoundKeepAlivePacket(ByteBuf in) {
        keepAliveId = in.readInt();
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
        out.writeInt(keepAliveId);
    }

}
