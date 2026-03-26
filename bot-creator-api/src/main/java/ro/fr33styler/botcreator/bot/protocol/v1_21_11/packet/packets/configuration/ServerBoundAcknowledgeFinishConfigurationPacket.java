package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundAcknowledgeFinishConfigurationPacket implements Packet {

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {}

}
