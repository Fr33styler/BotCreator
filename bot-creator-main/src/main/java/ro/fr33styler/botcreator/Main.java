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

            startBotLauncher(bots, workerGroup, arguments.getHost(), arguments.getPort(), arguments.getJoinDelay(), arguments.getRetryDelay());

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

    private static void startBotLauncher(Collection<Bot> bots, EventLoopGroup workerGroup, String host, int port, int joinDelay, int retryDelay) {
        Thread thread = new Thread(() -> {
            for (Bot bot : bots) {
                while (!bot.isLoggedIn()) {
                    if (!bot.isOnline()) {
                        bot.connect(workerGroup, host, port);
                    }
                    if (waitForLogin(bot, LOGIN_TIMEOUT_MS)) {
                        break;
                    }

                    LOGGER.warning(bot.getName() + " did not finish logging in, retrying...");
                    if (bot.isOnline()) {
                        bot.disconnect("Retrying login");
                    }

                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                try {
                    if (joinDelay > 0) {
                        Thread.sleep(joinDelay);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "BotLauncher");
        thread.setDaemon(true);
        thread.start();
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
