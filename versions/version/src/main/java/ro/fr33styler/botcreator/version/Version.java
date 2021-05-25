package ro.fr33styler.botcreator.version;

import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.util.logging.Logger;

public interface Version {

    PacketProtocol getProtocol(String name);

    SessionListener getListener(Logger logger);

    Packet getChatPacket(String message);
}
