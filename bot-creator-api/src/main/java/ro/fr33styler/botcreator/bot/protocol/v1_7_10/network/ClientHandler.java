package ro.fr33styler.botcreator.bot.protocol.v1_7_10.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.StageType;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.handshake.ServerBoundHandshakePacket;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.login.ClientBoundLoginFinishedPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.login.ClientBoundLoginOnlineModePacket;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.login.ServerBoundLoginStartPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_7_10.network.packets.play.*;

import java.util.logging.Level;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientOptions options;

    public ClientHandler(ClientOptions options) {
        this.options = options;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ServerBoundHandshakePacket(5, options.getHost(), options.getPort(), 2));
        ctx.writeAndFlush(new ServerBoundLoginStartPacket(options.getName()));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //Common
        if (msg instanceof AbstractDisconnectPacket) {
            options.getLogger().log(Level.INFO, "Disconnected: {0}", ((AbstractDisconnectPacket) msg).getReason());
        }

        //Login start
        if (msg instanceof ClientBoundLoginOnlineModePacket) {
            ctx.channel().disconnect();
            options.getLogger().info("Disconnected: The server is in online mode!");
            return;
        }
        if (msg instanceof ClientBoundLoginFinishedPacket) {
            options.setStage(StageType.PLAY_STAGE);
            options.setLoggedIn(true);
            ctx.writeAndFlush(new ServerBoundRespawnPacket());
        }
        //Login end

        //Play Start
        if (msg instanceof ClientBoundKeepAlivePacket) {
            ctx.writeAndFlush(new ServerBoundKeepAlivePacket(((ClientBoundKeepAlivePacket) msg).getKeepAliveId()));
        }
        if (msg instanceof ClientBoundRespawnScreenPacket && ((ClientBoundRespawnScreenPacket) msg).isDead()) {
            ctx.writeAndFlush(new ServerBoundRespawnPacket());
        }
        if (msg instanceof ClientBoundSystemChatPacket) {
            ClientBoundSystemChatPacket packet = (ClientBoundSystemChatPacket) msg;
            if (!packet.getMessage().isEmpty()) {
                options.getLogger().log(Level.INFO, "Received Message: {0}", packet.getMessage());
            }
        }

        //Play End
        super.channelRead(ctx, msg);
    }

}
