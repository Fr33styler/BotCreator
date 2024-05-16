package ro.fr33styler.botcreator.bot;

import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.tcp.TcpClientSession;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.time.Instant;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot {


    private static final BitSet EMPTY_BYTE_SET = new BitSet();

    private Session session;
    private final String name;
    private final Logger logger;

    public Bot(String name, Logger parentLogger) {
        this.name = name;

        this.logger = Logger.getLogger(name);
        this.logger.setParent(parentLogger);
    }

    public void connect(String host, int port) {

        session = new TcpClientSession(host, port, new MinecraftProtocol(name));

        session.addListener(new SessionAdapter() {

            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ClientboundSystemChatPacket) {
                    StringBuilder text = new StringBuilder();
                    ComponentFlattener.textOnly().flatten(((ClientboundSystemChatPacket) packet).getContent(), text::append);
                    logger.log(Level.INFO, "Received Message: {0}", text);
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                StringBuilder text = new StringBuilder();
                ComponentFlattener.textOnly().flatten(event.getReason(), text::append);
                logger.log(Level.INFO, "Disconnected: {0}", text);
            }

        });

        session.connect();
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

    public void disconnect() {
        if (isOnline()) {
            session.disconnect("Disconnect");
        }
    }

    public String getName() {
        return name;
    }

}