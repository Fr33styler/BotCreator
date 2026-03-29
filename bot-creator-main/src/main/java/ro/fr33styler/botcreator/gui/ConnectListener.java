package ro.fr33styler.botcreator.gui;

import io.netty.channel.EventLoopGroup;
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

    private final JButton connect;
    private final JTextField hostInput;
    private final JTextField portInput;
    private final JTextField clientsInput;
    private final JComboBox<String> versionsBox;

    private final JComboBox<String> botsBox;

    public ConnectListener(Logger logger, Deque<Bot> bots, EventLoopGroup workerGroup, JTextField hostInput, JTextField portInput, JTextField clientsInput, JComboBox<String> versionsBox, JButton connect, JComboBox<String> botsBox) {
        this.logger = logger;
        this.bots = bots;
        this.workerGroup = workerGroup;

        this.hostInput = hostInput;
        this.portInput = portInput;
        this.clientsInput = clientsInput;
        this.versionsBox = versionsBox;

        this.connect = connect;
        this.botsBox = botsBox;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        int port;
        try {
            port = Integer.parseInt(portInput.getText());
        } catch (NumberFormatException exception) {
            logger.severe("The port must be a number!");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(clientsInput.getText());
        } catch (NumberFormatException exception) {
            logger.severe("The number of clients must be a number!");
            return;
        }

        if (amount < 0) {
            logger.severe("The number of clients must be 0 or higher!");
            return;
        }

        connect.setEnabled(false);
        connect.setText("Connecting...");

        String host = hostInput.getText();

        while (bots.size() < amount) {
            int id = bots.size();

            ProtocolVersion version = ProtocolVersion.getByVersion((String) versionsBox.getSelectedItem());
            Logger botLogger = Logger.getLogger("Bot_" + id);
            botLogger.setParent(logger);
            bots.addLast(version.getProtocol().newBot(botLogger, "Bot_" + id));
            botsBox.addItem("Bot_" + id);
        }
        while (bots.size() > amount) {
            Bot bot = bots.removeLast();
            bot.disconnect("You left the server!");
            botsBox.removeItem(bot.getName());
        }
        for (Bot client : bots) {
            if (!client.isOnline()) {
                client.connect(workerGroup, host, port);
            }
        }
        connect.setText("Connect");
        connect.setEnabled(true);
    }

}
