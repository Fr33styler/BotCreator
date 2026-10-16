package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundLoginFinishedPacketPacket implements Packet {

    public ClientBoundLoginFinishedPacketPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public void encode(ByteBuf out) {}

}
