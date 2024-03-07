package ro.fr33styler.botcreator;

import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.logger.LogHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("BotCreator");

    private static final Deque<Bot> clients = new ArrayDeque<>();

    public static void main(String[] args) {

        if (Double.parseDouble(System.getProperty("java.class.version")) < 61) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null,
                    "You need Java 17 or higher to run this!",
                    "Version not supported!", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        sendInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER && clientsBox.getSelectedItem() instanceof String) {
                    String selected = (String) clientsBox.getSelectedItem();
                    String text = sendInput.getText();
                    for (Bot bot : clients) {
                        if (selected.equals("All") || selected.equals(bot.getName())) {
                            if (text.startsWith("/")) {
                                bot.executeCommand(text.substring(1));
                            } else {
                                bot.sendMessage(text);
                            }
                        }
                    }
                    sendInput.setText("/");
                }
            }

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

        JTextField clientsInput = new JTextField("1", 3);
        topPanel.add(clientsInput);

        JButton connect = new JButton("Connect");
        topPanel.add(connect);

        connect.addActionListener(action -> {

            int port;
            try {
                port = Integer.parseInt(portInput.getText());
            } catch (NumberFormatException exception) {
                LOGGER.log(Level.SEVERE, "The port must be a number!");
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(clientsInput.getText());
            } catch (NumberFormatException exception) {
                LOGGER.log(Level.SEVERE, "The clients must be a number!");
                return;
            }

            connect.setEnabled(false);
            connect.setText("Connecting...");

            String host = hostInput.getText();

            while (clients.size() < amount) {
                int id = clients.size();
                clients.addLast(new Bot("Bot_" + id, LOGGER));
                clientsBox.addItem("Bot_" + id);
            }
            while (clients.size() > amount) {
                Bot bot = clients.removeLast();
                bot.disconnect();
                clientsBox.removeItem(bot.getName());
            }
            for (Bot client : clients) {
                if (!client.isOnline()) {
                    client.connect(host, port);
                }
            }
            connect.setText("Connect");
            connect.setEnabled(true);
        });

        frame.add(topPanel, BorderLayout.PAGE_START);

        frame.pack();
        frame.setVisible(true);

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> LOGGER.log(Level.SEVERE, throwable.getMessage(), throwable));
        LOGGER.info("Bot Creator has started!");

    }

}
