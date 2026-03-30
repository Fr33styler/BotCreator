package ro.fr33styler.botcreator;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.arguments.Arguments;
import ro.fr33styler.botcreator.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final Logger LOGGER = Logger.getLogger("BotCreator");

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
                String name = "Bot_" + (i + 1);
                Logger botLogger = Logger.getLogger(name);
                botLogger.setParent(LOGGER);
                Bot bot = arguments.getVersion().getProtocol().newBot(botLogger, name);
                bot.connect(workerGroup, arguments.getHost(), arguments.getPort());
                if (bot.isOnline()) {
                    bots.add(bot);
                }
            }

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
                        if (message.startsWith("/")) {
                            bot.executeCommand(message);
                        } else {
                            bot.sendMessage(message);
                        }
                    }
                }
            }
        } else {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

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

}