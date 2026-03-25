package ro.fr33styler.botcreator.bot.protocol;

import ro.fr33styler.botcreator.bot.protocol.v1_12_2.v1_12_2;
import ro.fr33styler.botcreator.bot.protocol.v1_18_2.v1_18_2;
import ro.fr33styler.botcreator.bot.protocol.v1_19_4.v1_19_4;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.v1_20_6;
import ro.fr33styler.botcreator.bot.protocol.v1_21_11.v1_21_11;
import ro.fr33styler.botcreator.bot.protocol.v1_8_9.v1_8_9;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum ProtocolVersion {

    V1_21_11("1.21.11", v1_21_11::new),
    V1_20_6("1.20.6", v1_20_6::new),
    V1_19_4("1.19.4", v1_19_4::new),
    V1_18_2("1.18.2", v1_18_2::new),
    V1_12_2("1.12.2", v1_12_2::new),
    V1_8_9("1.8.9", v1_8_9::new);

    private static final Map<String, ProtocolVersion> PROTOCOL_VERSIONS = new HashMap<>();

    private final String version;
    private final Supplier<Protocol> supplier;

    static {
        for (ProtocolVersion protocolVersion : ProtocolVersion.values()) {
            PROTOCOL_VERSIONS.put(protocolVersion.getVersion(), protocolVersion);
        }
    }

    ProtocolVersion(String version, Supplier<Protocol> supplier) {
        this.version = version;
        this.supplier = supplier;
    }

    public String getVersion() {
        return version;
    }

    public Protocol getProtocol() {
        return supplier.get();
    }

    public static ProtocolVersion getByVersion(String version) {
        return PROTOCOL_VERSIONS.get(version);
    }

}
