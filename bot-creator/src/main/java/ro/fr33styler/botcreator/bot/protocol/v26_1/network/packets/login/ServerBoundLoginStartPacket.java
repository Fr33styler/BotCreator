package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.UUID;

public class ServerBoundLoginStartPacket implements Packet {

    private final String name;
    private final UUID uniqueId;

    public ServerBoundLoginStartPacket(String name, UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, name);
        ByteBufUtil.writeUuid(out, uniqueId);
    }

}
