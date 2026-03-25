package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ConfirmTeleportation implements Packet {

    private final int teleportId;

    public ConfirmTeleportation(int teleportId) {
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
