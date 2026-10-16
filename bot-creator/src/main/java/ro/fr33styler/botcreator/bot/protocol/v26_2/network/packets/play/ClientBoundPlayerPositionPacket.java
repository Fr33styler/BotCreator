package ro.fr33styler.botcreator.bot.protocol.v26_2.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;

public class ClientBoundPlayerPositionPacket implements Packet {

    private final int teleportId;

    public ClientBoundPlayerPositionPacket(ByteBuf in) {
        teleportId = ByteBufUtil.readVarInt(in);
    }

    @Override
    public int getId() {
        return 0x48;
    }

    public int getTeleportId() {
        return teleportId;
    }

    @Override
    public void encode(ByteBuf out) {}

}
