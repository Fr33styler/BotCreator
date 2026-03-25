package ro.fr33styler.botcreator.bot.protocol.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.Arrays;
import java.util.zip.Deflater;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private final ClientOptions options;
    private final Deflater compresser = new Deflater();

    public PacketEncoder(ClientOptions options) {
        this.options = options;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf out) {
        ByteBuf tempBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(tempBuf, packet.getId());

        packet.encode(tempBuf);

        if (options.compressed()) {
            int uncompressedLength = tempBuf.readableBytes();
            byte[] input = new byte[uncompressedLength];
            tempBuf.readBytes(input);

            compresser.setInput(input);
            compresser.finish();

            byte[] output = new byte[uncompressedLength];
            int compressedLength = compresser.deflate(output);
            compresser.reset();

            if (compressedLength < options.getMaximumPacketSize()) {
                ByteBufUtil.writeVarInt(tempBuf, 0);
                tempBuf.writeBytes(input);
            } else {
                ByteBufUtil.writeVarInt(tempBuf, uncompressedLength);
                tempBuf.writeBytes(Arrays.copyOf(output, compressedLength));
            }
        }

        ByteBufUtil.writeVarInt(out, tempBuf.readableBytes());
        out.writeBytes(tempBuf);
    }

}
