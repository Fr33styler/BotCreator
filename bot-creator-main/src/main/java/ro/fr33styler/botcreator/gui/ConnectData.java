package ro.fr33styler.botcreator.gui;

import javax.swing.*;
import java.util.function.Supplier;

public class ConnectData {

    private Supplier<String> host;
    private Supplier<String> port;
    private Supplier<String> clients;
    private Supplier<String> joinDelay;
    private Supplier<String> retryDelay;
    private Supplier<String> version;
    private JButton connectButton;
    private JComboBox<String> botsBox;

    private ConnectData() {}

    public String getHost() {
        return host.get();
    }

    public String getPort() {
        return port.get();
    }

    public String getClients() {
        return clients.get();
    }

    public String getJoinDelay() {
        return joinDelay.get();
    }

    public String getRetryDelay() {
        return retryDelay.get();
    }

    public String getVersion() {
        return version.get();
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JComboBox<String> getBotsBox() {
        return botsBox;
    }

    public static ConnectDataBuilder builder() {
        return new ConnectDataBuilder();
    }

    public static class ConnectDataBuilder {

        private static final Supplier<String> EMPTY = () -> "";

        private Supplier<String> host = EMPTY;
        private Supplier<String> port = EMPTY;
        private Supplier<String> clients = EMPTY;
        private Supplier<String> joinDelay = EMPTY;
        private Supplier<String> retryDelay = EMPTY;
        private Supplier<String> version = EMPTY;
        private JButton connectButton;
        private JComboBox<String> botsBox;

        private ConnectDataBuilder() {}

        public ConnectDataBuilder host(JTextField hostInput) {
            this.host = hostInput::getText;
            return this;
        }

        public ConnectDataBuilder port(JTextField portInput) {
            this.port = portInput::getText;
            return this;
        }

        public ConnectDataBuilder clients(JTextField clientsInput) {
            this.clients = clientsInput::getText;
            return this;
        }

        public ConnectDataBuilder joinDelay(JTextField joinDelayInput) {
            this.joinDelay = joinDelayInput::getText;
            return this;
        }

        public ConnectDataBuilder retryDelay(JTextField retryDelayInput) {
            this.retryDelay = retryDelayInput::getText;
            return this;
        }

        public ConnectDataBuilder version(JComboBox<String> versionsBox) {
            this.version = () -> (String) versionsBox.getSelectedItem();
            return this;
        }

        public ConnectDataBuilder connect(JButton connectButton) {
            this.connectButton = connectButton;
            return this;
        }

        public ConnectDataBuilder botsBox(JComboBox<String> botsBox) {
            this.botsBox = botsBox;
            return this;
        }

        public ConnectData build() {
            ConnectData connectData = new ConnectData();
            connectData.host = host;
            connectData.port = port;
            connectData.clients = clients;
            connectData.joinDelay = joinDelay;
            connectData.retryDelay = retryDelay;
            connectData.version = version;
            connectData.connectButton = connectButton;
            connectData.botsBox = botsBox;

            return connectData;
        }

    }

}
