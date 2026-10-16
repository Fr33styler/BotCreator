package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.UUID;

public class ClientBoundResourcePackPushPlayPacket implements Packet {

    private final UUID uniqueId;

    public ClientBoundResourcePackPushPlayPacket(ByteBuf in) {
        uniqueId = ByteBufUtil.readUuid(in);
    }

    @Override
    public int getId() {
        return 0x4F;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
