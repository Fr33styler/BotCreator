package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.UUID;

public class ClientBoundResourcePackPushPacket implements Packet {

    private final UUID uniqueId;

    public ClientBoundResourcePackPushPacket(ByteBuf in) {
        uniqueId = ByteBufUtil.readUuid(in);
    }

    @Override
    public int getId() {
        return 0x09;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
