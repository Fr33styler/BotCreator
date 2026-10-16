package ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundPlayDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundPlayDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromJson(in));
    }

    @Override
    public int getId() {
        return 0x40;
    }

}
