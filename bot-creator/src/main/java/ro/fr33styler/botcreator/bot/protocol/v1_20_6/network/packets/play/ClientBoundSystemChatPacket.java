package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundSystemChatPacket implements Packet {

    private final String message;
    private final boolean actionMessage;

    public ClientBoundSystemChatPacket(ByteBuf in) {
        message = ByteBufUtil.readTextFromNbt(in);
        actionMessage = in.readBoolean();
    }

    @Override
    public int getId() {
        return 0x6C;
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
