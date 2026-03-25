package ro.fr33styler.botcreator.bot.protocol;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

public class ClientOptions {

    private final Logger logger;
    private final String name;
    private final String host;
    private final int port;

    private Stage stage;
    private boolean compressed;
    private int maximumPacketSize;

    public ClientOptions(Stage stage, Logger logger, String name, String host, int port) {
        setStage(stage);
        this.logger = logger;
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean compressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public int getMaximumPacketSize() {
        return maximumPacketSize;
    }

    public void setMaximumPacketSize(int maximumPacketSize) {
        this.maximumPacketSize = maximumPacketSize;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        if (stage == null) throw new IllegalArgumentException("Stage cannot be null");
        this.stage = stage;
    }

}
