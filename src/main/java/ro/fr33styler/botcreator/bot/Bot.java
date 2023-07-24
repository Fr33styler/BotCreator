package ro.fr33styler.botcreator.bot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import net.kyori.adventure.text.flattener.ComponentFlattener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot {

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
                if (packet instanceof ClientboundChatPacket) {
                    StringBuilder text = new StringBuilder();
                    ComponentFlattener.textOnly().flatten(((ClientboundChatPacket) packet).getMessage(), text::append);
                    logger.log(Level.INFO, "Received Message: {0}", text);
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                StringBuilder text = new StringBuilder();
                ComponentFlattener.textOnly().flatten(DefaultComponentSerializer.get().deserialize(event.getReason()), text::append);
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
            session.send(new ServerboundChatPacket(message));
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