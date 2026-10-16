package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundChatPacket implements Packet {

    private final String message;

    public ServerBoundChatPacket(String message) {
        this.message = message;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, message);
    }

}
