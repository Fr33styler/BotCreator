package ro.fr33styler.botcreator.bot.protocol.v1_18_2.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.UUID;

public class ClientBoundSystemChatPacket implements Packet {

    private final String message;
    private final int messageType;
    private final UUID uniqueId;

    public ClientBoundSystemChatPacket(ByteBuf in) {
        message = ByteBufUtil.readTextFromJson(in.readBytes(in.readableBytes() - 17));
        messageType = in.readByte();
        uniqueId = ByteBufUtil.readUuid(in);
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

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
