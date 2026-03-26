package ro.fr33styler.botcreator.bot;

import io.netty.channel.EventLoopGroup;

public interface Bot {

    String getName();

    boolean isOnline();

    void sendMessage(String message);

    void executeCommand(String command);

    void connect(EventLoopGroup workerGroup, String host, int port);

    void disconnect(String reason);

}
