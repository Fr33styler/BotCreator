package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundLoginAcknowledgedPacket implements Packet {

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void encode(ByteBuf out) {}

}
