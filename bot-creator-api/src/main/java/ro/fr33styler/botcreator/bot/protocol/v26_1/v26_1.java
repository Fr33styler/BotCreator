package ro.fr33styler.botcreator.bot.protocol.v26_1;

import ro.fr33styler.botcreator.bot.Bot;
import ro.fr33styler.botcreator.bot.protocol.Protocol;

import java.util.logging.Logger;

public class v26_1 implements Protocol {

    @Override
    public Bot newBot(Logger logger, String name) {
        return new BotImpl(logger, name);
    }

}
