package ro.fr33styler.botcreator.bot.protocol.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PacketDecoder extends ReplayingDecoder<Packet> {

    private final ClientOptions options;
    private final Inflater decompresser = new Inflater();

    public PacketDecoder(ClientOptions options) {
        this.options = options;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) {
        ByteBuf byteBuf = in.readBytes(ByteBufUtil.readVarInt(in));

        if (options.compressed()) {
            int uncompressed = ByteBufUtil.readVarInt(byteBuf);

            if (uncompressed > 0) {

                byte[] compressedBytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(compressedBytes);

                decompresser.setInput(compressedBytes);

                try {
                    byte[] result = new byte[uncompressed];

                    decompresser.inflate(result);
                    decompresser.reset();

                    byteBuf.writeBytes(result);
                } catch (DataFormatException ignored) {}
            }
        }

        options.getStage().create(byteBuf, list);
    }

}
