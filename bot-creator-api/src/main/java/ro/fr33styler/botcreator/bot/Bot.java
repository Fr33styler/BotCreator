package ro.fr33styler.botcreator.bot;

import io.netty.channel.EventLoopGroup;

import java.util.logging.Logger;

public interface Bot {

    String getName();

    Logger getLogger();

    boolean isOnline();

    boolean isLoggedIn();

    void sendMessage(String message);

    void executeCommand(String command);

    void connect(EventLoopGroup workerGroup, String host, int port);

    void disconnect(String reason);

}
