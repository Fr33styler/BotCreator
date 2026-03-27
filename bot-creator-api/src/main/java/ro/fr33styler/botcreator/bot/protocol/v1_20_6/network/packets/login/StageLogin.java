package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.login;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.Stage;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StageLogin implements Stage {

    private final Map<Integer, Function<ByteBuf, Packet>> packets = new HashMap<>();

    public StageLogin() {
        packets.put(0x00, ClientBoundLoginDisconnectPacket::new);
        packets.put(0x02, ClientBoundLoginFinishedPacket::new);
        packets.put(0x03, ClientBoundLoginCompressionPacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }

}
