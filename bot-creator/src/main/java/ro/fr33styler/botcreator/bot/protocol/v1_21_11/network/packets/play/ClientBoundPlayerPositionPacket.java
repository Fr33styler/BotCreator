package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundPlayerPositionPacket implements Packet {

    private final int teleportId;

    public ClientBoundPlayerPositionPacket(ByteBuf in) {
        teleportId = ByteBufUtil.readVarInt(in);
    }

    @Override
    public int getId() {
        return 0x46;
    }

    public int getTeleportId() {
        return teleportId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
