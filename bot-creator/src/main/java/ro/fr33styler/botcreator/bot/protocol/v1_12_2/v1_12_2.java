package ro.fr33styler.botcreator.bot.protocol.v1_12_2;

import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.Protocol;

import java.util.logging.Logger;

public class v1_12_2 implements Protocol {

    @Override
    public Bot newBot(Logger logger, String name) {
        return new BotImpl(logger, name);
    }

}
