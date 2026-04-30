package ro.fr33styler.botcreator.gui;

import io.netty.channel.EventLoopGroup;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.ProtocolVersion;
import ro.fr33styler.botcreator.logger.LogHandler;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

public class Gui {

    private final Logger logger;
    private final Deque<Bot> bots = new ArrayDeque<>();
    private final JComboBox<String> botsBox = new JComboBox<>();

    public Gui(Logger logger) {
        this.logger = logger;
    }

    public JPanel getTopPanel(EventLoopGroup workerGroup) {
        JPanel topPanel = new JPanel();

        topPanel.add(new JLabel("Host: "));

        JTextField hostInput = new JTextField("127.0.0.1", 10);
        topPanel.add(hostInput);

        topPanel.add(new JLabel("Port: "));
        JTextField portInput = new JTextField("25565", 5);
        topPanel.add(portInput);

        topPanel.add(new JLabel("Clients: "));

        JTextField clientsInput = new JTextField("1", 3);
        topPanel.add(clientsInput);

        topPanel.add(new JLabel("Delay (ms): "));
        JTextField joinDelayInput = new JTextField("1000", 5);
        topPanel.add(joinDelayInput);

        topPanel.add(new JLabel("Retry (ms): "));
        JTextField retryDelayInput = new JTextField("3000", 5);
        topPanel.add(retryDelayInput);

        JComboBox<String> versionsBox = new JComboBox<>();
        for (ProtocolVersion version : ProtocolVersion.values()) {
            versionsBox.addItem(version.getVersion());
        }
        topPanel.add(versionsBox);

        JButton connect = new JButton("Connect");

        connect.addActionListener(new ConnectListener(logger, bots, workerGroup,
                hostInput, portInput, clientsInput, joinDelayInput, retryDelayInput, versionsBox, connect, botsBox));

        topPanel.add(connect);

        return topPanel;
    }

    public JScrollPane getCenterPanel() {
        JScrollPane centerPane = new JScrollPane();
        centerPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextArea logArea = new JTextArea(10, 80);
        logArea.setEditable(false);
        centerPane.getViewport().setView(logArea);

        logger.addHandler(new LogHandler(logArea));

        return centerPane;
    }

    public JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel();

        botsBox.addItem("All");
        bottomPanel.add(botsBox);

        JTextField sendInput = new JTextField("/", 64);
        bottomPanel.add(sendInput);

        sendInput.addKeyListener(new KeyListener(bots, sendInput, botsBox));

        return bottomPanel;
    }

}
