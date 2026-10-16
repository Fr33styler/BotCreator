package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ServerBoundAcceptTeleportationPacket implements Packet {

    private final int teleportId;

    public ServerBoundAcceptTeleportationPacket(int teleportId) {
        this.teleportId = teleportId;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeVarInt(out, teleportId);
    }

}
