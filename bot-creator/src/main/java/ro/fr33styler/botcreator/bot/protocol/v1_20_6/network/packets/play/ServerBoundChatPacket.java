package ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.Arrays;
import java.util.BitSet;

public class ServerBoundChatPacket implements Packet {

    private final String message;
    private final long timestamp;
    private final long salt;
    private final byte[] signature;
    private final int offset;
    private final BitSet acknowledgedMessages;

    public ServerBoundChatPacket(String message, long timestamp, long salt, byte[] signature, int offset, BitSet acknowledgedMessages) {
        this.message = message;
        this.timestamp = timestamp;
        this.salt = salt;
        this.signature = signature;
        this.offset = offset;
        this.acknowledgedMessages = acknowledgedMessages;
    }

    @Override
    public int getId() {
        return 0x06;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, message);
        out.writeLong(timestamp);
        out.writeLong(salt);
        if (signature == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeBytes(signature);
        }

        ByteBufUtil.writeVarInt(out, offset);
        byte[] bytes = acknowledgedMessages.toByteArray();
        out.writeBytes(Arrays.copyOf(bytes, -Math.floorDiv(-20, 8)));
    }

}
