package ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundResourcePackPacket implements Packet {

    private final String hash;

    public ServerBoundResourcePackPacket(String hash) {
        this.hash = hash;
    }

    @Override
    public int getId() {
        return 0x19;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, hash);
        ByteBufUtil.writeVarInt(out, 0);
    }

}
