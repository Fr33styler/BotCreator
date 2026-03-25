package ro.fr33styler.botcreator.bot.protocol.v1_19_4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketDecoder;
import ro.fr33styler.botcreator.bot.protocol.coder.PacketEncoder;
import ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.ClientHandler;
import ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.play.ServerBoundChat;
import ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.play.ServerBoundChatCommand;
import ro.fr33styler.botcreator.bot.protocol.v1_19_4.packet.packets.StageType;

import java.time.Instant;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotImpl implements Bot {

    private static final BitSet EMPTY_BYTE_SET = new BitSet();

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
            channel.writeAndFlush(new ServerBoundChat(message, Instant.now().toEpochMilli(), 0L, null, 0, EMPTY_BYTE_SET));
        }
    }

    @Override
    public void executeCommand(String command) {
        if (isOnline()) {
            channel.writeAndFlush(new ServerBoundChatCommand(command, Instant.now().toEpochMilli(), 0L, null, 0, EMPTY_BYTE_SET));
        }
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

        ChannelFuture channelFuture = bootstrap.connect(host, port);

        channelFuture.syncUninterruptibly();

        channel = channelFuture.channel();
    }

    @Override
    public void disconnect() {
        channel.disconnect();
        logger.log(Level.INFO, "Disconnected: You left the server!");
    }

}
