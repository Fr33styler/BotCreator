package ro.fr33styler.botcreator.bot.protocol.v1_19_4;

import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.Protocol;

import java.util.logging.Logger;

public class v1_19_4 implements Protocol {

    @Override
    public Bot newBot(Logger logger, String name) {
        return new BotImpl(logger, name);
    }

}
