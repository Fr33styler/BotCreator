package ro.fr33styler.botcreator.bot.protocol.v1_19_4.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundResourcePackPushPacket implements Packet {

    public ClientBoundResourcePackPushPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x40;
    }

    @Override
    public void encode(ByteBuf out) {}

}
