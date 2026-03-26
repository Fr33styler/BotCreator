package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.common.AbstractDisconnectPacket;

public class ClientBoundLoginDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundLoginDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromJson(in));
    }

    @Override
    public int getId() {
        return 0x00;
    }

}
