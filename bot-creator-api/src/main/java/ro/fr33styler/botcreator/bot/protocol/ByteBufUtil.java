package ro.fr33styler.botcreator.bot.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteBufUtil {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;
    private static final byte[] TEXT_START = { '"', 't', 'e', 'x', 't', '"', ':' };
    private static final byte[] EXTRA_START = {'"', 'e', 'x', 't', 'r', 'a', '"', ':'};
    private static final Pattern UNICODE_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    private ByteBufUtil() {}

    public static String readTextFromNbt(ByteBuf buf) {
        try {
            ByteBufInputStream input = new ByteBufInputStream(buf);
            int typeId = input.readUnsignedByte();
            if (typeId == 0) {
                return null;
            } else {
                NbtType<?> type = NbtType.byId(typeId);
                StringBuilder builder = new StringBuilder();
                readTextFromNBT((new NBTInputStream(input)).readValue(type, 512), builder);
                return transformUnicode(builder.toString());
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private static void readTextFromNBT(Object tag, StringBuilder builder) {
        if (tag instanceof String) {
            builder.append((String) tag);
        } else if (tag instanceof NbtList) {
            for (Object element : (NbtList<?>) tag) {
                readTextFromNBT(element, builder);
            }
        } else if (tag instanceof NbtMap) {
            for (Map.Entry<String, Object> entry : ((NbtMap) tag).entrySet()) {
                switch (entry.getKey()) {
                    case "":
                    case "text":
                    case "extra":
                        readTextFromNBT(entry.getValue(), builder);
                        break;
                }
            }
        }
    }

    public static String readTextFromJson(ByteBuf buf) {
        byte[] array = new byte[readVarInt(buf)];
        buf.readBytes(array);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        if (array[0] == '"') {
            readStringFromJson(array, 1, buffer);
        } else {
            readTextFromJson(array, 0, buffer);
        }

        return transformUnicode(new String(buffer.toByteArray(), StandardCharsets.UTF_8));
    }

    private static int readTextFromJson(byte[] array, int index, ByteArrayOutputStream buffer) {
        for (int i = index; i < array.length; i++) {
            if (array[i] == '}') {
                return i;
            } else if (array[i] == ':' && endsAtWith(array, i + 1, TEXT_START)) {
                i = readStringFromJson(array, i + 2, buffer);
            } else if (array[i] == '[' && endsAtWith(array, i, EXTRA_START)) {
                boolean append = false;
                for (int j = i + 1; j < array.length; j++) {
                    byte character = array[j];
                    if (character == ']') {
                        i = j;
                        break;
                    }
                    if (character == '"' && (j == 0 || array[j - 1] != '\\')) {
                        append = !append;
                    } else if (append) {
                        buffer.write(character);
                    } else if (character == '{') {
                        i = j = readTextFromJson(array, j + 1, buffer);
                    }
                }
            } else if (array[i] == '{') {
                i = readTextFromJson(array, i + 1, buffer);
            }
        }
        return index;
    }

    private static int readStringFromJson(byte[] array, int index, ByteArrayOutputStream buffer) {
        for (int i = index; i < array.length; i++) {
            byte character = array[i];
            if (character == '"' && (i == 0 || array[i - 1] != '\\')) {
                return i;
            } else {
                buffer.write(character);
            }
        }
        return index;
    }

    private static boolean endsAtWith(byte[] array, int index, byte[] characters) {
        if (index < characters.length) return false;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] != array[index + i - characters.length]) {
                return false;
            }
        }
        return true;
    }

    private static String transformUnicode(String text) {
        Matcher matcher = UNICODE_PATTERN.matcher(text);
        while (matcher.find()) {
            String group = matcher.group(1);
            text = text.replace("\\u" + group, String.valueOf((char) Integer.parseInt(group, 16)));
        }
        return text;
    }

    public static UUID readUuid(ByteBuf byteBuf) {
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }

    public static void writeUuid(ByteBuf byteBuf, UUID uniqueId) {
        byteBuf.writeLong(uniqueId.getMostSignificantBits());
        byteBuf.writeLong(uniqueId.getLeastSignificantBits());
    }

    public static String readString(ByteBuf byteBuf) {
        return byteBuf.readString(ByteBufUtil.readVarInt(byteBuf), StandardCharsets.UTF_8);
    }

    public static void writeString(ByteBuf byteBuf, String string) {
        ByteBufUtil.writeVarInt(byteBuf, string.length());
        byteBuf.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public static int readVarInt(ByteBuf byteBuf) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = byteBuf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static void writeVarInt(ByteBuf byteBuf, int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                byteBuf.writeByte(value);
                return;
            }

            byteBuf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
        }
    }

}
