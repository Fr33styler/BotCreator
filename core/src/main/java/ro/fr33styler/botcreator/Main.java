package ro.fr33styler.botcreator;

import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.logger.LogHandler;
import ro.fr33styler.botcreator.version.GameVersion;
import ro.fr33styler.botcreator.version.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("BotCreator");
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private static Bot[] clients = new Bot[0];

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                LOGGER.log(Level.SEVERE, null, throwable)
        );

        JFrame frame = new JFrame("BotCreator");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Bottom
        JPanel bottomPanel = new JPanel();

        JComboBox<String> clientsBox = new JComboBox<>();
        clientsBox.addItem("All");
        bottomPanel.add(clientsBox);

        JTextField sendInput = new JTextField("/", 64);
        bottomPanel.add(sendInput);

        sendInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String selected = clientsBox.getSelectedItem().toString();
                    for (Bot bot : clients) {
                        if (selected.equals("All") || selected.equals(bot.getName())) {
                            bot.sendMessage(sendInput.getText());
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}

        });

        frame.add(bottomPanel, BorderLayout.PAGE_END);

        //Center
        JScrollPane centerPane = new JScrollPane();
        centerPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextArea logArea = new JTextArea(10, 80);
        logArea.setEditable(false);
        centerPane.getViewport().setView(logArea);

        LOGGER.addHandler(new LogHandler(logArea));

        frame.add(centerPane, BorderLayout.CENTER);

        //Top
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Host: "));

        JTextField hostInput = new JTextField("127.0.0.1", 10);
        topPanel.add(hostInput);

        topPanel.add(new JLabel("Port: "));
        JTextField portInput = new JTextField("25565", 5);
        topPanel.add(portInput);

        topPanel.add(new JLabel("Clients: "));

        JTextField amountInput = new JTextField("1", 3);
        topPanel.add(amountInput);

        topPanel.add(new JLabel("Version: "));

        JComboBox<String> versionBox = new JComboBox<>();
        for (GameVersion version : GameVersion.values()) {
            versionBox.addItem(version.getID());
        }
        versionBox.addItemListener(action -> {
            Version version = GameVersion.getByName(action.getItem().toString());
            for (Bot bot : clients) {
                bot.setVersion(version);
            }
        });
        topPanel.add(versionBox);

        JButton connectButton = new JButton("Connect");
        topPanel.add(connectButton);

        connectButton.addActionListener((action) -> {

            connectButton.setEnabled(false);
            connectButton.setText("Connecting...");

            String host = hostInput.getText();

            int port = Integer.parseInt(portInput.getText());
            int amount = Integer.parseInt(amountInput.getText());
            Version version = GameVersion.getByName(versionBox.getSelectedItem().toString());
            EXECUTOR_SERVICE.submit(() -> {
                if (clients.length != amount) {
                    Bot[] newArray = new Bot[amount];
                    if (amount > clients.length) {
                        for (int i = clients.length; i < amount; i++) {
                            newArray[i] = new Bot(version, "Bot_" + i, LOGGER);
                            clientsBox.addItem("Bot_" + i);
                        }
                        System.arraycopy(clients, 0, newArray, 0, clients.length);
                    } else {
                        System.arraycopy(clients, 0, newArray, 0, amount);
                        for (int i = amount; i < clients.length; i++) {
                            clients[i].disconnect();
                            clientsBox.removeItem("Bot_" + i);
                        }
                    }
                    clients = newArray;
                }
                for (Bot client : clients) {
                    if (!client.isOnline()) {
                        client.connect(host, port);
                    }
                }
                connectButton.setText("Connect");
                connectButton.setEnabled(true);
            });
        });

        frame.add(topPanel, BorderLayout.PAGE_START);

        frame.pack();
        frame.setVisible(true);

        LOGGER.info("Starting program");

    }

}
