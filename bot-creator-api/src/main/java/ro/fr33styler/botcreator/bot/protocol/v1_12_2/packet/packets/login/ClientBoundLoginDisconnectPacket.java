package ro.fr33styler.botcreator.bot.protocol.v1_12_2.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.v1_12_2.packet.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundLoginDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundLoginDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromJson(in));
    }

    @Override
    public int getId() {
        return 0x00;
    }

}
