package ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class PlayDisconnect extends AbstractDisconnectPacket {

    public PlayDisconnect(ByteBuf in) {
        super(ByteBufUtil.readTextFromNbt(in));
    }

    @Override
    public int getId() {
        return 0x1D;
    }

}
