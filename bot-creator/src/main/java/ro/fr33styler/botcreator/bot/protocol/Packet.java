package ro.fr33styler.botcreator.bot.protocol;

import io.netty.buffer.ByteBuf;

public interface Packet {

    int getId();

    void encode(ByteBuf out);

}
