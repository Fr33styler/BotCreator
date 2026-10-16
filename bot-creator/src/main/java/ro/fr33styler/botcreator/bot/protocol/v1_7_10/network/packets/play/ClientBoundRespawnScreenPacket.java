package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundRespawnScreenPacket implements Packet {

    private final float health;

    public ClientBoundRespawnScreenPacket(ByteBuf in) {
        health = in.readFloat();
    }

    @Override
    public int getId() {
        return 0x06;
    }

    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void encode(ByteBuf out) {}

}
