package ro.fr33styler.botcreator.bot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import net.kyori.adventure.text.flattener.ComponentFlattener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot {


    private static final List<ArgumentSignature> EMPTY_LIST = new ArrayList<>(0);
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

    public void executeCommand(String message) {
        if (isOnline()) {
            session.send(new ServerboundChatCommandPacket(message, Instant.now().toEpochMilli(), 0L, EMPTY_LIST, 0, EMPTY_BYTE_SET));
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