package ro.fr33styler.botcreator.bot.protocol.v1_8_9.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundSystemChatMessagePacket implements Packet {

    private final String message;
    private final int messageType;

    public ClientBoundSystemChatMessagePacket(ByteBuf in) {
        message = ByteBufUtil.readTextFromJson(in.readBytes(in.readableBytes() - 1));
        messageType = in.readByte();
    }

    @Override
    public int getId() {
        return 0x02;
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
