package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.play;

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
        packets.put(0x2C, ClientBoundKeepAlivePacket::new);
        packets.put(0x48, ClientBoundSynchronizePlayerPositionPacket::new);
        packets.put(0x79, ClientBoundSystemChatMessagePacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }



}
