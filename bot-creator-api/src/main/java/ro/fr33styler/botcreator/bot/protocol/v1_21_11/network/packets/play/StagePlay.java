package ro.fr33styler.botcreator.bot.protocol.v1_21_11.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.Stage;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StagePlay implements Stage {

    private final Map<Integer, Function<ByteBuf, Packet>> packets = new HashMap<>();

    public StagePlay() {
        packets.put(0x20, ClientBoundPlayDisconnectPacket::new);
        packets.put(0x2B, ClientBoundKeepAlivePacket::new);
        packets.put(0x46, ClientBoundPlayerPositionPacket::new);
        packets.put(0x77, ClientBoundSystemChatPacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }



}
