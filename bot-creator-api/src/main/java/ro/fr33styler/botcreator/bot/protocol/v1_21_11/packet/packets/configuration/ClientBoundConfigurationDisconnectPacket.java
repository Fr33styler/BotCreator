package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

public class ClientBoundConfigurationDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundConfigurationDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromNbt(in));
    }

    @Override
    public int getId() {
        return 0x02;
    }

}
