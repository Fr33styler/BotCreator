package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StagePlay implements Stage {

    private final Map<Integer, Function<ByteBuf, Packet>> packets = new HashMap<>();

    public StagePlay() {
        packets.put(0x00, ClientBoundKeepAlivePacket::new);
        packets.put(0x02, ClientBoundSystemChatPacket::new);
        packets.put(0x06, ClientBoundRespawnScreenPacket::new);
        packets.put(0x40, ClientBoundPlayDisconnectPacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }



}
