package ro.fr33styler.botcreator.bot.protocol;

import io.netty.buffer.ByteBuf;

import java.util.List;

public interface Stage {

    void create(ByteBuf byteBuf, List<Object> packets);

}
