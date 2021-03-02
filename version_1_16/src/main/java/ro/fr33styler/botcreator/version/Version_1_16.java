package ro.fr33styler.botcreator.version;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Version_1_16 implements Version {

    @Override
    public PacketProtocol getProtocol(String name) {
        return new MinecraftProtocol(name);
    }

    @Override
    public SessionListener getListener(Logger logger) {
        return new SessionAdapter() {

            @Override
            public void disconnected(DisconnectedEvent disconnectedEvent) {
                String reason = disconnectedEvent.getReason();
                logger.log(Level.INFO, "Disconnected: {0}", reason);
            }


            @Override
            public void packetReceived(PacketReceivedEvent receiveEvent) {
                if (receiveEvent.getPacket() instanceof ServerChatPacket) {
                    ServerChatPacket chatPacket = receiveEvent.getPacket();
                    logger.log(Level.INFO, "Received Message: {0}", chatPacket.getMessage().toString());
                }
            }

        };
    }

    @Override
    public Packet getChatPacket(String message) {
        return new ClientChatPacket(message);
    }

}
