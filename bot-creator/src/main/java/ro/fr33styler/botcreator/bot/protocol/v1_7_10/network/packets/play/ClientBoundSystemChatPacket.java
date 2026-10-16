package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundSystemChatPacket implements Packet {

    private final String message;

    public ClientBoundSystemChatPacket(ByteBuf in) {
        message = ByteBufUtil.readTextFromJson(in);
    }

    @Override
    public int getId() {
        return 0x02;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void encode(ByteBuf out) {}

}
