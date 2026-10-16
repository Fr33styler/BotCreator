package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.UUID;

public class ServerBoundResourcePackPlayPacket implements Packet {

    private final UUID uniqueId;

    public ServerBoundResourcePackPlayPacket(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int getId() {
        return 0x31;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeUuid(out, uniqueId);
        ByteBufUtil.writeVarInt(out, 0);
    }

}
