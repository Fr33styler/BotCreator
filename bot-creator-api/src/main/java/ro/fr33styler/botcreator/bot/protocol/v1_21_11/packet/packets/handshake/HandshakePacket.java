package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.handshake;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class HandshakePacket implements Packet {

    private final int version;
    private final String host;
    private final int port;
    private final int intent;

    public HandshakePacket(int version, String host, int port, int intent) {
        this.version = version;
        this.host = host;
        this.port = port;
        this.intent = intent;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, version);
        ByteBufUtil.writeString(out, host);
        out.writeShort(port);
        ByteBufUtil.writeVarInt(out, intent);
    }

}
