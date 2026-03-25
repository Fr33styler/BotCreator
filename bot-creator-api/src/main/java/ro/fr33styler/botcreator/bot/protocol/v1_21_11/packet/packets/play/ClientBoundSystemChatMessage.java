package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundSystemChatMessage implements Packet {

    private final String message;
    private final boolean actionMessage;

    public ClientBoundSystemChatMessage(ByteBuf in) {
        message = ByteBufUtil.readTextFromNbt(in);
        actionMessage = in.readBoolean();
    }

    @Override
    public int getId() {
        return 0x77;
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
