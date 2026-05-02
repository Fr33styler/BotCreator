package ro.fr33styler.botcreator.gui;

import io.netty.channel.EventLoopGroup;
import ro.fr33styler.botcreator.launcher.BotLauncher;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.ProtocolVersion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.logging.Logger;

public class ConnectListener implements ActionListener {

    private final Logger logger;
    private final Deque<Bot> bots;
    private final EventLoopGroup workerGroup;

    private final ConnectData connectData;

    private BotLauncher launcher;

    public ConnectListener(Logger logger, Deque<Bot> bots, EventLoopGroup workerGroup, ConnectData connectData) {
        this.logger = logger;
        this.bots = bots;
        this.workerGroup = workerGroup;

        this.connectData = connectData;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        int port;
        try {
            port = Integer.parseInt(connectData.getPort());
        } catch (NumberFormatException exception) {
            logger.severe("The port must be a number!");
            return;
        }

        int clients;
        try {
            clients = Integer.parseInt(connectData.getClients());
        } catch (NumberFormatException exception) {
            logger.severe("The number of clients must be a number!");
            return;
        }

        if (clients < 0) {
            logger.severe("The number of clients must be 0 or higher!");
            return;
        }

        int joinDelay;
        try {
            joinDelay = Integer.parseInt(connectData.getJoinDelay());
        } catch (NumberFormatException exception) {
            logger.severe("The join delay must be a number!");
            return;
        }

        if (joinDelay < 0) {
            logger.severe("The join delay must be 0 or higher!");
            return;
        }

        int retryDelay;
        try {
            retryDelay = Integer.parseInt(connectData.getRetryDelay());
        } catch (NumberFormatException exception) {
            logger.severe("The retry delay must be a number!");
            return;
        }

        if (retryDelay < 1000) {
            logger.severe("The retry delay must be 1000 or higher!");
            return;
        }

        JButton connect = connectData.getConnectButton();
        JComboBox<String> botsBox = connectData.getBotsBox();

        connect.setEnabled(false);
        connect.setText("Connecting...");

        if (launcher != null) {
            launcher.stop();
        }

        String host = connectData.getHost();

        bots.removeIf(bot -> !bot.isOnline());
        while (bots.size() < clients) {
            int id = bots.size();
            ProtocolVersion version = ProtocolVersion.getByVersion(connectData.getVersion());
            Logger botLogger = Logger.getLogger("Bot_" + id);
            botLogger.setParent(logger);
            bots.addLast(version.getProtocol().newBot(botLogger, "Bot_" + id));
            botsBox.addItem("Bot_" + id);
        }
        while (bots.size() > clients) {
            Bot bot = bots.removeLast();
            bot.disconnect("You left the server!");
            botsBox.removeItem(bot.getName());
        }

        launcher = new BotLauncher(bots, workerGroup, host, port, joinDelay, retryDelay);
        launcher.start();

        connect.setText("Connect");
        connect.setEnabled(true);
    }
}