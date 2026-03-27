package ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets;

import ro.fr33styler.botcreator.bot.protocol.Stage;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.configuration.StageConfiguration;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.login.StageLogin;
import ro.fr33styler.botcreator.bot.protocol.v26_1.network.packets.play.StagePlay;

public class StageType {

    public static final Stage LOGIN_STAGE = new StageLogin();
    public static final Stage CONFIGURATION_STAGE = new StageConfiguration();
    public static final Stage PLAY_STAGE = new StagePlay();

    private StageType() {}

}
