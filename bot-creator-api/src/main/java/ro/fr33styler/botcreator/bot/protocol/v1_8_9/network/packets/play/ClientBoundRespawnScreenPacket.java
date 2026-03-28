package ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundRespawnScreenPacket implements Packet {

    private final int status;

    public ClientBoundRespawnScreenPacket(ByteBuf in) {
        status = ByteBufUtil.readVarInt(in);
    }

    @Override
    public int getId() {
        return 0x42;
    }

    public boolean isDead() {
        return status == 2;
    }

    @Override
    public void encode(ByteBuf out) {}

}
