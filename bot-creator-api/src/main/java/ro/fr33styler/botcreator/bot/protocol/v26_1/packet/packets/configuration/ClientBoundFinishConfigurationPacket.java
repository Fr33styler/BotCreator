package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundFinishConfigurationPacket implements Packet {

    public ClientBoundFinishConfigurationPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {}

}
