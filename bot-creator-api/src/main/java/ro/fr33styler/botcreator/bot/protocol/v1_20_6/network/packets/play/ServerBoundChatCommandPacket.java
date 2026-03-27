package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ServerBoundChatCommandPacket implements Packet {

    private final String command;

    public ServerBoundChatCommandPacket(String command) {
        this.command = command;
    }

    @Override
    public int getId() {
        return 0x4;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, command);
    }

}
