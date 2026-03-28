package ro.fr33styler.botcreator.bot.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

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
                return transformUnicode(readTextFromNBT((new NBTInputStream(input)).readValue(type, 512)));
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private static String readTextFromNBT(Object tag) {
        String current = "";
        if (tag instanceof NbtMap) {
            NbtMap map = (NbtMap) tag;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (entry.getKey().isEmpty()) {
                    current = current.concat(readTextFromNBT(value));
                }
                if (entry.getKey().equals("text")) {
                    current = current.concat(readTextFromNBT(value));
                }
                if (entry.getKey().equals("extra")) {
                    if (value instanceof NbtList) {
                        for (Object element : (NbtList<?>) value) {
                            current = current.concat(readTextFromNBT(element));
                        }
                    }
                }
            }
        } else if (tag instanceof String) {
            current = (String) tag;
        }
        return current;
    }

    public static String readTextFromJson(ByteBuf buf) {
        StringBuilder builder = new StringBuilder();

        byte[] array = new byte[buf.readableBytes()];
        buf.readBytes(array);

        if (array[1] == '"') {
            readStringFromJson(array, 2, builder);
        } else {
            readTextFromJson(array, 0, builder);
        }

        return transformUnicode(builder.toString());
    }

    private static int readTextFromJson(byte[] array, int index, StringBuilder builder) {
        for (int i = index; i < array.length; i++) {
            if (array[i] == '}') {
                return i;
            } else if (array[i] == ':' && isMatching(array, i + 1, TEXT_START)) {
                i = readStringFromJson(array, i + 2, builder);
            } else if (array[i] == '[' && isMatching(array, i, EXTRA_START)) {
                boolean skip = false;
                boolean append = false;
                for (int j = i + 1; j < array.length; j++) {
                    byte character = array[j];
                    if (character == ']') {
                        i = j;
                        break;
                    }
                    if (character == '\\') {
                        skip = true;
                    } else if (!skip && character == '"') {
                        append = !append;
                    } else if (append) {
                        skip = false;
                        builder.append((char) character);
                    } else if (character == '{') {
                        i = j = readTextFromJson(array, j + 1, builder);
                    }
                }
            } else if (array[i] == '{') {
                i = readTextFromJson(array, i + 1, builder);
            }
        }
        return index;
    }

    private static boolean isMatching(byte[] array, int index, byte[] characters) {
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

    private static int readStringFromJson(byte[] array, int index, StringBuilder builder) {
        boolean skip = false;
        for (int i = index; i < array.length; i++) {
            byte character = array[i];
            if (character == '\\') {
                skip = true;
            } else if (!skip && character == '"') {
                return i;
            } else {
                skip = false;
                builder.append((char) character);
            }
        }
        return index;
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
