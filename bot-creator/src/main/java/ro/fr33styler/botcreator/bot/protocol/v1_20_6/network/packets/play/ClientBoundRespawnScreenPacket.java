package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundRespawnScreenPacket implements Packet {

    public ClientBoundRespawnScreenPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x3C;
    }

    @Override
    public void encode(ByteBuf out) {}

}
