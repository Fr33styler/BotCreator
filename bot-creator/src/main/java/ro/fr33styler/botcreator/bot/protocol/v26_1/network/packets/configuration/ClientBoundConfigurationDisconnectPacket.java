package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.common.AbstractDisconnectPacket;

public class ClientBoundConfigurationDisconnectPacket extends AbstractDisconnectPacket {

    public ClientBoundConfigurationDisconnectPacket(ByteBuf in) {
        super(ByteBufUtil.readTextFromNbt(in));
    }

    @Override
    public int getId() {
        return 0x02;
    }

}
