package ro.fr33styler.botcreator.bot;

import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.time.Instant;
import java.util.BitSet;

public class Bot {

    private static final BitSet EMPTY_BYTE_SET = new BitSet();

    private ClientSession session;
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
            session.send(new ServerboundChatPacket(message, Instant.now().toEpochMilli(), 0L, null, 0, EMPTY_BYTE_SET, 0));
        }
    }

    public void executeCommand(String command) {
        if (isOnline()) {
            session.send(new ServerboundChatCommandPacket(command));
        }
    }

    public void connect(String host, int port) {
        session = ClientNetworkSessionFactory.factory()
                .setAddress(host, port).setProtocol(new MinecraftProtocol(name)).create();
        session.addListener(new BotSession(name));
        session.connect();
    }

    public void disconnect() {
        if (isOnline()) {
            session.disconnect("Disconnect");
        }
    }

}