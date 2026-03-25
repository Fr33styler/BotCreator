package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.configuration;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.Stage;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StageConfiguration implements Stage {

    private final Map<Integer, Function<ByteBuf, Packet>> packets = new HashMap<>();

    public StageConfiguration() {
        packets.put(0x02, ConfigurationDisconnect::new);
        packets.put(0x03, FinishConfiguration::new);
        packets.put(0x0E, ClientBoundSelectKnownPacksPacket::new);
    }

    @Override
    public void create(ByteBuf byteBuf, List<Object> list) {
        int id = ByteBufUtil.readVarInt(byteBuf);
        if (packets.containsKey(id)) {
            list.add(packets.get(id).apply(byteBuf));
        }
    }

}
