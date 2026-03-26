package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.common.AbstractDisconnectPacket;

public class ClientBoundPlayDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundPlayDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromNbt(in));
    }

    @Override
    public int getId() {
        return 0x20;
    }

}
