package ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets;

import ro.fr33styler.botcreator.bot.protocol.Stage;
import ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.configuration.StageConfiguration;
import ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.login.StageLogin;
import ro.fr33styler.botcreator.bot.protocol.v26_1.packet.packets.play.StagePlay;

public class StageType {

    public static final Stage LOGIN_STAGE = new StageLogin();
    public static final Stage CONFIGURATION_STAGE = new StageConfiguration();
    public static final Stage PLAY_STAGE = new StagePlay();

    private StageType() {}

}
