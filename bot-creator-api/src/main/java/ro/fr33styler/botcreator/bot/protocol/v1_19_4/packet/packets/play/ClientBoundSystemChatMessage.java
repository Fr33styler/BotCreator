package ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundSystemChatMessage implements Packet {

    private final String message;
    private final boolean actionMessage;

    public ClientBoundSystemChatMessage(ByteBuf in) {
        message = ByteBufUtil.readTextFromJson(in.readBytes(in.readableBytes() - 1));
        actionMessage = in.readBoolean();
    }

    @Override
    public int getId() {
        return 0x64;
    }

    public String getMessage() {
        return message;
    }

    public boolean isActionMessage() {
        return actionMessage;
    }

    @Override
    public void encode(ByteBuf out) {}

}
