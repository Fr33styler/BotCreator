package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.Packet;

import java.util.Arrays;
import java.util.zip.Deflater;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf out) {
        ByteBuf tempBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(tempBuf, packet.getId());

        packet.encode(tempBuf);

        ByteBufUtil.writeVarInt(out, tempBuf.readableBytes());
        out.writeBytes(tempBuf);
    }

}
