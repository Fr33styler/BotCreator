package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundFinishConfigurationPacket implements Packet {

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {}

}
