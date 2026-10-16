package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Packet> {

    private final ClientOptions options;

    public PacketDecoder(ClientOptions options) {
        this.options = options;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) {
        ByteBuf byteBuf = in.readBytes(ByteBufUtil.readVarInt(in));

        options.getStage().create(byteBuf, list);
    }

}
