package ro.fr33styler.botcreator.bot.protocol.v1_19_4.network.packets.play;

import io.netty.buffer.ByteBuf;
import ro.fr33styler.botcreator.bot.protocol.Packet;
import ro.fr33styler.botcreator.bot.protocol.ByteBufUtil;

import java.util.Arrays;
import java.util.BitSet;

public class ServerBoundChatCommandPacket implements Packet {

    private final String command;
    private final long timestamp;
    private final long salt;
    private final byte[] signature;
    private final int offset;
    private final BitSet acknowledgedMessages;

    public ServerBoundChatCommandPacket(String command, long timestamp, long salt, byte[] signature, int offset, BitSet acknowledgedMessages) {
        this.command = command;
        this.timestamp = timestamp;
        this.salt = salt;
        this.signature = signature;
        this.offset = offset;
        this.acknowledgedMessages = acknowledgedMessages;
    }

    @Override
    public int getId() {
        return 0x4;
    }

    @Override
    public void encode(ByteBuf out) {
        ByteBufUtil.writeString(out, command);
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
