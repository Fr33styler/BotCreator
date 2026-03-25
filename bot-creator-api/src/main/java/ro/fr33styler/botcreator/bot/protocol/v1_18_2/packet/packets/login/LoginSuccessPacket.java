package ro.fr33styler.botcreator.bot.protocol.v1_18_2.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class LoginSuccessPacket implements Packet {

    public LoginSuccessPacket(ByteBuf in) {}

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public void encode(ByteBuf out) {}

}
