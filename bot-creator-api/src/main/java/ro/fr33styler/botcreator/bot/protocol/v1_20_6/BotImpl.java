package ro.fr33styler.botcreator.bot.protocol.v1_20_6;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketDecoder;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketEncoder;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.ClientHandler;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.StageType;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play.ServerBoundChatPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.network.packets.play.ServerBoundChatCommandPacket;

import java.time.Instant;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotImpl implements Bot {

    private static final BitSet EMPTY_BYTE_SET = new BitSet();

    private Channel channel;
    private final Logger logger;
    private final String name;
    private ClientOptions options;

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
    public boolean isLoggedIn() {
        return options != null && options.isLoggedIn() && isOnline();
    }

    @Override
    public void sendMessage(String message) {
        if (isOnline()) {
            channel.writeAndFlush(new ServerBoundChatPacket(message, Instant.now().toEpochMilli(), 0L, null, 0, EMPTY_BYTE_SET));
        }
    }

    @Override
    public void executeCommand(String command) {
        if (isOnline()) {
            channel.writeAndFlush(new ServerBoundChatCommandPacket(command));
        }
    }

    @Override
    public void connect(EventLoopGroup workerGroup, String host, int port) {

        options = new ClientOptions(StageType.LOGIN_STAGE, logger, name, host, port);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

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
        if (channel != null) {
            channel.disconnect();
        }
        logger.log(Level.INFO, "Disconnected: {0}", reason);
    }

}
