package ro.fr33styler.botcreator.bot.protocol.v1_8_9;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketDecoder;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketEncoder;
import ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.ClientHandler;
import ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.StageType;
import ro.fr33styler.botcreator.bot.protocol.v1_8_9.network.packets.play.ServerBoundChatPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BotImpl implements Bot {

    private Channel channel;
    private final Logger logger;
    private final String name;

    public BotImpl(Logger logger, String name) {
        this.logger = logger;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        return channel != null && channel.isActive();
    }

    @Override
    public void sendMessage(String message) {
        if (isOnline()) {
            channel.writeAndFlush(new ServerBoundChatPacket(message));
        }
    }

    @Override
    public void executeCommand(String command) {
        sendMessage("/" + command);
    }

    @Override
    public void connect(EventLoopGroup workerGroup, String host, int port) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            private final ClientOptions options = new ClientOptions(StageType.LOGIN_STAGE, logger, name, host, port);

            @Override
            public void initChannel(SocketChannel channel) {
                channel.pipeline().addLast(new PacketEncoder(options), new PacketDecoder(options), new ClientHandler(options));
            }

        });

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port);

            channelFuture.syncUninterruptibly();
            channel = channelFuture.channel();
        } catch (Exception exception) {
            logger.log(Level.WARNING, "Disconnected: {0}", exception.getMessage());
        }
    }

    @Override
    public void disconnect(String reason) {
        channel.disconnect();
        logger.log(Level.INFO, "Disconnected: {0}", reason);
    }

}
