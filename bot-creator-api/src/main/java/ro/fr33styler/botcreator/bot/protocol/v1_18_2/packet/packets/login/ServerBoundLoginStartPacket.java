package ro.fr33styler.botcreator.bot.protocol.v1_18_2.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ServerBoundLoginStartPacket implements Packet {

    private final String name;

    public ServerBoundLoginStartPacket(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, name);
    }

}
