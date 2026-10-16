package ro.fr33styler.botcreator.bot.protocol.v26_3.network.packets.play;

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
        packets.put(0x20, ClientBoundPlayDisconnectPacket::new);
        packets.put(0x2D, ClientBoundKeepAlivePacket::new);
        packets.put(0x45, ClientBoundRespawnScreenPacket::new);
        packets.put(0x49, ClientBoundPlayerPositionPacket::new);
        packets.put(0x52, ClientBoundResourcePackPushPlayPacket::new);
        packets.put(0x7C, ClientBoundSystemChatPacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }



}
