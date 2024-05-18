package ro.fr33styler.botcreator.bot;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.tcp.TcpClientSession;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.time.Instant;
import java.util.BitSet;

public class Bot {

    private static final BitSet EMPTY_BYTE_SET = new BitSet();

    private Session session;
    private final String name;

    public Bot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return session != null && session.isConnected();
    }

    public void sendMessage(String message) {
        if (isOnline()) {
            session.send(new ServerboundChatPacket(message, Instant.now().toEpochMilli(), 0L, null, 0, EMPTY_BYTE_SET));
        }
    }

    public void executeCommand(String command) {
        if (isOnline()) {
            session.send(new ServerboundChatCommandPacket(command));
        }
    }

    public void connect(String host, int port) {
        session = new TcpClientSession(host, port, new MinecraftProtocol(name));
        session.addListener(new BotSession(name));
        session.connect();
    }

    public void disconnect() {
        if (isOnline()) {
            session.disconnect("Disconnect");
        }
    }

}