package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundChatCommandPacket implements Packet {

    private final String command;

    public ServerBoundChatCommandPacket(String command) {
        this.command = command;
    }

    @Override
    public int getId() {
        return 0x7;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, command);
    }

}
