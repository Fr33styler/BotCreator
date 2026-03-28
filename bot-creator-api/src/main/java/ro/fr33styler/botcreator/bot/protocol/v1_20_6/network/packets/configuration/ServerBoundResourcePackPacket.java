package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.UUID;

public class ServerBoundResourcePackPacket implements Packet {

    private final UUID uniqueId;

    public ServerBoundResourcePackPacket(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int getId() {
        return 0x06;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeUuid(out, uniqueId);
        ByteBufUtil.writeVarInt(out, 0);
    }

}
