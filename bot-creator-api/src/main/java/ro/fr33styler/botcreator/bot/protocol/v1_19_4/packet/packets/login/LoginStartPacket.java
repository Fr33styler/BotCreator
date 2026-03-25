package ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.UUID;

public class LoginStartPacket implements Packet {

    private final String name;
    private final UUID uniqueId;

    public LoginStartPacket(String name, UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, name);
        out.writeBoolean(uniqueId != null);
        if (uniqueId != null) {
            ByteBufUtil.writeUuid(out, uniqueId);
        }
    }

}
