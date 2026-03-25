package ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ro.fr33styler.botcreator.bot.protocol.ClientOptions;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.StageType;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.common.AbstractDisconnectPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration.AcknowledgeFinishConfiguration;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration.ClientBoundSelectKnownPacksPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration.FinishConfiguration;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.configuration.ServerBoundSelectKnownPacksPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.handshake.HandshakePacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.login.LoginAcknowledged;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.login.LoginStartPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.login.LoginSuccessPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.login.SetCompressPacket;
import ro.fr33styler.botcreator.bot.protocol.v1_20_6.packet.packets.play.*;

import java.util.logging.Level;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientOptions options;

    public ClientHandler(ClientOptions options) {
        this.options = options;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new HandshakePacket(766, options.getHost(), options.getPort(), 2));
        ctx.writeAndFlush(new LoginStartPacket(options.getName(), options.getUniqueId()));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //Common
        if (msg instanceof AbstractDisconnectPacket) {
            options.getLogger().log(Level.INFO, "Disconnected: {0}", ((AbstractDisconnectPacket) msg).getReason());
        }

        //Login start
        if (msg instanceof SetCompressPacket) {
            options.setCompressed(true);
            options.setMaximumPacketSize(((SetCompressPacket) msg).getMaximumPacketSize());
        }
        if (msg instanceof LoginSuccessPacket) {
            options.setStage(StageType.CONFIGURATION_STAGE);
            ctx.writeAndFlush(new LoginAcknowledged());
        }
        //Login end

        //Configuration Start
        if (msg instanceof ClientBoundSelectKnownPacksPacket) {
            ctx.writeAndFlush(new ServerBoundSelectKnownPacksPacket());
        }
        if (msg instanceof FinishConfiguration) {
            options.setStage(StageType.PLAY_STAGE);
            ctx.writeAndFlush(new AcknowledgeFinishConfiguration());
        }
        //Configuration End

        //Play Start
        if (msg instanceof SynchronizePlayerPositionPacket) {
            ctx.writeAndFlush(new ConfirmTeleportation(((SynchronizePlayerPositionPacket) msg).getTeleportId()));
        }
        if (msg instanceof ClientBoundKeepAlivePacket) {
            ctx.writeAndFlush(new ServerBoundKeepAlivePacket(((ClientBoundKeepAlivePacket) msg).getKeepAliveId()));
        }
        if (msg instanceof ClientBoundSystemChatMessage) {
            ClientBoundSystemChatMessage packet = (ClientBoundSystemChatMessage) msg;
            if (!packet.isActionMessage() && !packet.getMessage().isEmpty()) {
                options.getLogger().log(Level.INFO, "Received Message: {0}", packet.getMessage());
            }
        }

        //Play End
        super.channelRead(ctx, msg);
    }

}