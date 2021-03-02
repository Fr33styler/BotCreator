package ro.fr33styler.botcreator.bot;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import ro.fr33styler.botcreator.version.Version;

import java.util.logging.Logger;

public class Bot {

    private Session session;
    private Version version;
    private final String name;
    private final Logger logger;

    public Bot(Version version, String name, Logger parentLogger) {
        this.name = name;
        this.version = version;

        this.logger = Logger.getLogger(name);
        this.logger.setParent(parentLogger);
    }

    public void connect(String host, int port) {
        Client client = new Client(host, port, version.getProtocol(name), new TcpSessionFactory());
        this.session = client.getSession();
        client.getSession().addListener(version.getListener(logger));
        client.getSession().connect();
    }

    public void sendMessage(String message) {
        if (isOnline()) {
            session.send(version.getChatPacket(message));
        }
    }

    public boolean isOnline() {
        return session != null && session.isConnected();
    }

    public void disconnect() {
        if (isOnline()) {
            session.disconnect("Disconnect");
        }
    }

    public String getName() {
        return name;
    }

    public void setVersion(Version version) {
        disconnect();
        this.version = version;
    }
}
