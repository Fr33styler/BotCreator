package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundLoginOnlineModePacket implements Packet {

    public ClientBoundLoginOnlineModePacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void encode(ByteBuf out) {}

}
