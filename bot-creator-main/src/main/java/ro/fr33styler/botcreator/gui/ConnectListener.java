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

    private static final long LOGIN_TIMEOUT_MS = 30000L;

    private final Logger logger;
    private final Deque<Bot> bots;
    private final EventLoopGroup workerGroup;

    private final JButton connect;
    private final JTextField hostInput;
    private final JTextField portInput;
    private final JTextField clientsInput;
    private final JTextField joinDelayInput;
    private final JTextField retryDelayInput;
    private final JTextField maxOnlineInput;
    private final JComboBox<String> versionsBox;

    private final JComboBox<String> botsBox;

    public ConnectListener(Logger logger, Deque<Bot> bots, EventLoopGroup workerGroup, JTextField hostInput, JTextField portInput, JTextField clientsInput, JTextField joinDelayInput, JTextField retryDelayInput, JTextField maxOnlineInput, JComboBox<String> versionsBox, JButton connect, JComboBox<String> botsBox) {
        this.logger = logger;
        this.bots = bots;
        this.workerGroup = workerGroup;

        this.hostInput = hostInput;
        this.portInput = portInput;
        this.clientsInput = clientsInput;
        this.joinDelayInput = joinDelayInput;
        this.retryDelayInput = retryDelayInput;
        this.maxOnlineInput = maxOnlineInput;
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

        int joinDelay;
        try {
            joinDelay = Integer.parseInt(joinDelayInput.getText());
            if (joinDelay < 0) throw new NumberFormatException();
        } catch (NumberFormatException exception) {
            logger.severe("The join delay must be a non-negative number!");
            return;
        }

        int retryDelay;
        try {
            retryDelay = Integer.parseInt(retryDelayInput.getText());
            if (retryDelay < 0) throw new NumberFormatException();
        } catch (NumberFormatException exception) {
            logger.severe("The retry delay must be a non-negative number!");
            return;
        }

        int maxOnline;
        try {
            maxOnline = Integer.parseInt(maxOnlineInput.getText());
            if (maxOnline < 0) throw new NumberFormatException();
        } catch (NumberFormatException exception) {
            logger.severe("Max online must be a non-negative number!");
            return;
        }

        connect.setEnabled(false);
        connect.setText("Connecting...");

        String host = hostInput.getText();

        bots.removeIf(bot -> !bot.isOnline());
        while (bots.size() < amount) {
            int id = bots.size();
            ProtocolVersion version = ProtocolVersion.getByVersion((String) versionsBox.getSelectedItem());
            Logger botLogger = Logger.getLogger("Bot_" + id);
            botLogger.setParent(logger);
            Bot bot = version.getProtocol().newBot(botLogger, "Bot_" + id);
            bots.addLast(bot);
            botsBox.addItem("Bot_" + id);
        }
        while (bots.size() > amount) {
            Bot bot = bots.removeLast();
            bot.disconnect("You left the server!");
            botsBox.removeItem(bot.getName());
        }

        logLauncherSettings(amount, joinDelay, retryDelay, maxOnline);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                startBotLauncher(host, port, joinDelay, retryDelay, maxOnline);
                return null;
            }

            @Override
            protected void done() {
                connect.setText("Connect");
                connect.setEnabled(true);
            }
        }.execute();
    }

    private void startBotLauncher(String host, int port, int joinDelay, int retryDelay, int maxOnline) {
        while (!Thread.currentThread().isInterrupted()) {
            int activeLimit = getActiveLimit(bots.size(), maxOnline);
            int checked = 0;
            boolean allLoggedIn = true;

            disconnectAboveLimit(activeLimit);

            for (Bot bot : bots) {
                if (checked++ >= activeLimit) {
                    break;
                }
                if (bot.isLoggedIn()) {
                    continue;
                }

                allLoggedIn = false;
                connectOnce(bot, host, port, retryDelay);
                if (!sleep(joinDelay)) {
                    return;
                }
            }

            if (allLoggedIn && !sleep(Math.max(1000, retryDelay))) {
                return;
            }
        }
    }

    private void logLauncherSettings(int amount, int joinDelay, int retryDelay, int maxOnline) {
        int activeLimit = getActiveLimit(amount, maxOnline);
        String maxOnlineMessage = maxOnline > 0 ? String.valueOf(activeLimit) : "unlimited";
        logger.info("Launcher settings: clients=" + amount + ", maxOnline=" + maxOnlineMessage + ", joinDelay=" + joinDelay + "ms, retryDelay=" + retryDelay + "ms");
        if (maxOnline > 0 && activeLimit < amount) {
            logger.info("Max online limit active: only Bot_0 through Bot_" + (activeLimit - 1) + " will be kept online.");
        }
    }

    private static int getActiveLimit(int bots, int maxOnline) {
        return maxOnline > 0 ? Math.min(maxOnline, bots) : bots;
    }

    private void disconnectAboveLimit(int activeLimit) {
        int checked = 0;
        for (Bot bot : bots) {
            if (checked++ < activeLimit) {
                continue;
            }
            if (bot.isOnline()) {
                bot.disconnect("Max online limit reached");
            }
        }
    }

    private void connectOnce(Bot bot, String host, int port, int retryDelay) {
        if (!bot.isOnline()) {
            bot.connect(workerGroup, host, port);
        }

        if (waitForLogin(bot, LOGIN_TIMEOUT_MS)) {
            return;
        }

        logger.warning(bot.getName() + " did not finish logging in, retrying later...");
        if (bot.isOnline()) {
            bot.disconnect("Retrying login");
        }

        sleep(retryDelay);
    }

    private static boolean sleep(int delay) {
        if (delay <= 0) {
            return true;
        }
        try {
            Thread.sleep(delay);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private static boolean waitForLogin(Bot bot, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (bot.isLoggedIn()) {
                return true;
            }
            if (!bot.isOnline()) {
                return false;
            }
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return bot.isLoggedIn();
    }
}
