package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundSelectKnownPacksPacket implements Packet {

    public ClientBoundSelectKnownPacksPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x0E;
    }

    @Override
    public void encode(ByteBuf out) {}

}
