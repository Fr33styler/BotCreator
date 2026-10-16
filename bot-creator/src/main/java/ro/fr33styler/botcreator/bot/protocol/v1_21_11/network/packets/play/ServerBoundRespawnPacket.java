package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundRespawnPacket implements Packet {

    @Override
    public int getId() {
        return 0x0B;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, 0);
    }

}
