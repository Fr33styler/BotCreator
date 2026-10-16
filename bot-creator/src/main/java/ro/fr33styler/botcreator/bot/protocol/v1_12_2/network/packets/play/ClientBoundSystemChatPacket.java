package ro.fr33styler.botcreator.bot.protocol.v1_12_2.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundSystemChatPacket implements Packet {

    private final String message;
    private final int messageType;

    public ClientBoundSystemChatPacket(ByteBuf in) {
        message = ByteBufUtil.readTextFromJson(in);
        messageType = in.readByte();
    }

    @Override
    public int getId() {
        return 0x0F;
    }

    public String getMessage() {
        return message;
    }

    public boolean isActionMessage() {
        return messageType > 1;
    }

    @Override
    public void encode(ByteBuf out) {}

}
