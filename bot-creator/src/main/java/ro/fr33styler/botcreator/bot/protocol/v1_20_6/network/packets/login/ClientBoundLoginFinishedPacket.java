package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundLoginFinishedPacket implements Packet {

    public ClientBoundLoginFinishedPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public void encode(ByteBuf out) {}

}
