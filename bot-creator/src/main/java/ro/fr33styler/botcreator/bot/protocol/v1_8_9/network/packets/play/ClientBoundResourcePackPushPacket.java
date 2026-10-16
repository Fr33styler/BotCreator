package ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundResourcePackPushPacket implements Packet {

    private final String hash;

    public ClientBoundResourcePackPushPacket(ByteBuf in) {
        ByteBufUtil.readString(in);
        hash = ByteBufUtil.readString(in);
    }

    @Override
    public int getId() {
        return 0x48;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public void encode(ByteBuf out) {}

}
