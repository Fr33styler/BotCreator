package ro.fr33styler.botcreator.bot;

import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import ro.fr33styler.botcreator.Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BotSession extends SessionAdapter {

    private final Logger logger;

    public BotSession(String name) {
        this.logger = Logger.getLogger(name);
        this.logger.setParent(Main.LOGGER);
    }

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

}
