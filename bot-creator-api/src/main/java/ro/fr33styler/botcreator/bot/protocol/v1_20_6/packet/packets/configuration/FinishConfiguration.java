package ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class FinishConfiguration implements Packet {

    public FinishConfiguration(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {}

}
