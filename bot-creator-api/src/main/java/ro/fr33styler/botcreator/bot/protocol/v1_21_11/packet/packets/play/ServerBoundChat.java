package ro.fr33styler.botcreator.bot.protocol.v1_21_11.packet.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.Arrays;
import java.util.BitSet;

public class ServerBoundChat implements Packet {

    private final String message;
    private final long timestamp;
    private final long salt;
    private final byte[] signature;
    private final int messageCount;
    private final BitSet acknowledgedMessages;
    private final int checksum;

    public ServerBoundChat(String message, long timestamp, long salt, byte[] signature, int messageCount, BitSet acknowledgedMessages, int checksum) {
        this.message = message;
        this.timestamp = timestamp;
        this.salt = salt;
        this.signature = signature;
        this.messageCount = messageCount;
        this.acknowledgedMessages = acknowledgedMessages;
        this.checksum = checksum;
    }

    @Override
    public int getId() {
        return 0x08;
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
        ByteBufUtil.writeVarInt(out, messageCount);

        byte[] bytes = acknowledgedMessages.toByteArray();
        out.writeBytes(Arrays.copyOf(bytes, -Math.floorDiv(-20, 8)));

        out.writeByte(checksum);
    }

}
