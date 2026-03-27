package ro.fr33styler.botcreator.bot.protocol.v26_1.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.StageType;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration.ServerBoundFinishConfigurationPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration.ClientBoundSelectKnownPacksPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration.ClientBoundFinishConfigurationPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration.ServerBoundSelectKnownPacksPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.handshake.ServerBoundHandshakePacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login.ServerBoundLoginAcknowledgedPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login.ServerBoundLoginStartPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login.ClientBoundLoginFinishedPacketPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login.ClientBoundLoginCompressionPacketPacket;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.play.*;

import java.util.logging.Level;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientOptions options;

    public ClientHandler(ClientOptions options) {
        this.options = options;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ServerBoundHandshakePacket(775, options.getHost(), options.getPort(), 2));
        ctx.writeAndFlush(new ServerBoundLoginStartPacket(options.getName(), options.getUniqueId()));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //Common
        if (msg instanceof AbstractDisconnectPacket) {
            options.getLogger().log(Level.INFO, "Disconnected: {0}", ((AbstractDisconnectPacket) msg).getReason());
        }

        //Login start
        if (msg instanceof ClientBoundLoginCompressionPacketPacket) {
            options.setCompressed(true);
            options.setMaximumPacketSize(((ClientBoundLoginCompressionPacketPacket) msg).getMaximumPacketSize());
        }
        if (msg instanceof ClientBoundLoginFinishedPacketPacket) {
            options.setStage(StageType.CONFIGURATION_STAGE);
            ctx.writeAndFlush(new ServerBoundLoginAcknowledgedPacket());
        }
        //Login end

        //Configuration Start
        if (msg instanceof ClientBoundSelectKnownPacksPacket) {
            ctx.writeAndFlush(new ServerBoundSelectKnownPacksPacket());
        }
        if (msg instanceof ClientBoundFinishConfigurationPacket) {
            options.setStage(StageType.PLAY_STAGE);
            ctx.writeAndFlush(new ServerBoundFinishConfigurationPacket());
        }
        //Configuration End

        //Play Start
        if (msg instanceof ClientBoundPlayerPositionPacket) {
            ctx.writeAndFlush(new ServerBoundAcceptTeleportationPacket(((ClientBoundPlayerPositionPacket) msg).getTeleportId()));
        }
        if (msg instanceof ClientBoundKeepAlivePacket) {
            ctx.writeAndFlush(new ServerBoundKeepAlivePacket(((ClientBoundKeepAlivePacket) msg).getKeepAliveId()));
        }
        if (msg instanceof ClientBoundSystemChatPacket) {
            ClientBoundSystemChatPacket packet = (ClientBoundSystemChatPacket) msg;
            if (!packet.isActionMessage() && !packet.getMessage().isEmpty()) {
                options.getLogger().log(Level.INFO, "Received Message: {0}", packet.getMessage());
            }
        }

        //Play End
        super.channelRead(ctx, msg);
    }

}