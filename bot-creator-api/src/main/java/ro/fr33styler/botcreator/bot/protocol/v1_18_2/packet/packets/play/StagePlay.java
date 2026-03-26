package ro.fr33styler.botcreator.bot.protocol.v1_18_2.packet.packets.play;

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
        packets.put(0x1A, ClientBoundPlayDisconnectPacket::new);
        packets.put(0x21, ClientBoundKeepAlivePacket::new);
        packets.put(0x38, ClientBoundSynchronizePlayerPositionPacket::new);
        packets.put(0x0F, ClientBoundSystemChatMessagePacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }



}
