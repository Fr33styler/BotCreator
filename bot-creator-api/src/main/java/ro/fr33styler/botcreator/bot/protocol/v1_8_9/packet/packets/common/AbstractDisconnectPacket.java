package ro.fr33styler.botcreator.bot.protocol.v1_8_9.packet.packets.common;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public abstract class AbstractDisconnectPacket implements Packet {

    private final String reason;

    public AbstractDisconnectPacket(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void encode(ByteBuf out) {}

}
