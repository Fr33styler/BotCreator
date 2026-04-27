package ro.fr33styler.botcreator;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import ro.fr33styler.botcreator.arguments.Arguments;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final Logger LOGGER = Logger.getLogger("BotCreator");
    private static final long LOGIN_TIMEOUT_MS = 30000L;
    private static final long CONNECT_TIMEOUT_MS = 5000L;

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        if (args.length > 0) {
            Arguments arguments;
            List<Bot> bots = new ArrayList<>();

            try {
                arguments = Arguments.parse(args);
            } catch (IllegalArgumentException exception) {
                workerGroup.shutdownGracefully();
                LOGGER.severe(exception.getMessage());
                return;
            }

            LOGGER.info("Bot Creator has started!");

            for (int i = 0; i < arguments.getClients(); i++) {
                String name = "Bot_" + i;
                Logger botLogger = Logger.getLogger(name);
                botLogger.setParent(LOGGER);

                Bot bot = arguments.getVersion().getProtocol().newBot(botLogger, name);
                bots.add(bot);
            }

            logLauncherSettings(bots.size(), arguments.getJoinDelay(), arguments.getRetryDelay(), arguments.getMaxOnline());
            startBotLauncher(bots, workerGroup, arguments.getHost(), arguments.getPort(), arguments.getJoinDelay(), arguments.getRetryDelay(), arguments.getMaxOnline());

            if (bots.isEmpty()) {
                workerGroup.shutdownGracefully();
                LOGGER.severe("All of the bots failed to connect, closing!");
            } else {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    for (Bot bot : bots) {
                        if (!bot.isLoggedIn()) {
                            continue;
                        }
                        if (message.startsWith("/")) {
                            bot.executeCommand(message);
                        } else {
                            bot.sendMessage(message);
                        }
                    }
                }
            }
        } else {
            System.setProperty("sun.java2d.noddraw", "true");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            JFrame frame = new JFrame("BotCreator");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            Gui gui = new Gui(LOGGER);
            frame.add(gui.getTopPanel(workerGroup), BorderLayout.PAGE_START);
            frame.add(gui.getCenterPanel(), BorderLayout.CENTER);
            frame.add(gui.getBottomPanel(), BorderLayout.PAGE_END);

            frame.pack();
            frame.setVisible(true);

            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> LOGGER.log(Level.SEVERE, throwable.getMessage(), throwable));
            LOGGER.info("Bot Creator has started!");
        }
    }

    private static void startBotLauncher(Collection<Bot> bots, EventLoopGroup workerGroup, String host, int port, int joinDelay, int retryDelay, int maxOnline) {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                int activeLimit = getActiveLimit(bots.size(), maxOnline);
                int checked = 0;
                boolean allLoggedIn = true;

                disconnectAboveLimit(bots, activeLimit);

                for (Bot bot : bots) {
                    if (checked++ >= activeLimit) {
                        break;
                    }
                    if (bot.isLoggedIn()) {
                        continue;
                    }

                    allLoggedIn = false;
                    if (!connectOnce(bot, workerGroup, host, port, retryDelay) || !sleep(joinDelay)) {
                        return;
                    }
                }

                if (allLoggedIn && !sleep(Math.max(1000, retryDelay))) {
                    return;
                }
            }
        }, "BotLauncher");
        thread.setDaemon(true);
        thread.start();
    }

    private static void logLauncherSettings(int bots, int joinDelay, int retryDelay, int maxOnline) {
        int activeLimit = getActiveLimit(bots, maxOnline);
        String maxOnlineMessage = maxOnline > 0 ? String.valueOf(activeLimit) : "unlimited";
        LOGGER.info("Launcher settings: clients=" + bots + ", maxOnline=" + maxOnlineMessage + ", joinDelay=" + joinDelay + "ms, retryDelay=" + retryDelay + "ms");
        if (maxOnline > 0 && activeLimit < bots) {
            LOGGER.info("Max online limit active: only Bot_0 through Bot_" + (activeLimit - 1) + " will be kept online.");
        }
    }

    private static int getActiveLimit(int bots, int maxOnline) {
        return maxOnline > 0 ? Math.min(maxOnline, bots) : bots;
    }

    private static void disconnectAboveLimit(Collection<Bot> bots, int activeLimit) {
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

    private static boolean connectOnce(Bot bot, EventLoopGroup workerGroup, String host, int port, int retryDelay) {
        if (!bot.isOnline()) {
            bot.connect(workerGroup, host, port);
        }

        if (waitForLogin(bot, LOGIN_TIMEOUT_MS)) {
            return true;
        }

        if (Thread.currentThread().isInterrupted()) {
            return false;
        }

        LOGGER.warning(bot.getName() + " did not finish logging in, retrying later...");
        if (bot.isOnline()) {
            bot.disconnect("Retrying login");
        }

        return sleep(retryDelay);
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
        long now = System.currentTimeMillis();
        long deadline = now + timeoutMs;
        long connectDeadline = now + CONNECT_TIMEOUT_MS;
        boolean wasOnline = false;

        while (now < deadline) {
            if (bot.isLoggedIn()) {
                return true;
            }
            if (bot.isOnline()) {
                wasOnline = true;
            } else if (wasOnline || now >= connectDeadline) {
                return false;
            }

            if (!sleep(50)) {
                return false;
            }
            now = System.currentTimeMillis();
        }
        return bot.isLoggedIn();
    }
}
