package ro.fr33styler.botcreator.gui;

import ro.fr33styler.botcreator.bot.Bot;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyListener extends KeyAdapter {

    private int position = 0;
    private final JTextField input;
    private final Collection<Bot> bots;
    private final JComboBox<String> botsBox;
    private final List<String> history = new ArrayList<>();

    public KeyListener(Collection<Bot> bots, JTextField input, JComboBox<String> botsBox) {
        this.bots = bots;
        this.input = input;
        this.botsBox = botsBox;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_UP && position > 0) {
            input.setText(history.get(--position));
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN && position < history.size()) {
            input.setText(position + 1 == history.size() ? "/" : history.get(++position));
        } else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            String selected = (String) botsBox.getSelectedItem();

            String text = input.getText();
            for (Bot bot : bots) {
                if (selected == null || selected.equals("All") || selected.equals(bot.getName())) {
                    if (text.startsWith("/")) {
                        bot.executeCommand(text.substring(1));
                    } else {
                        bot.sendMessage(text);
                    }
                }
            }
            history.remove(text);
            history.add(text);

            input.setText("/");
            position = history.size();
        }
    }

}
