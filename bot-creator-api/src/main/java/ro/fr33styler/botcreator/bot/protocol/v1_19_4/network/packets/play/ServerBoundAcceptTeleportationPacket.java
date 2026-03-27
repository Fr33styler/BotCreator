package ro.fr33styler.botcreator.bot.protocol.v1_19_4.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

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
