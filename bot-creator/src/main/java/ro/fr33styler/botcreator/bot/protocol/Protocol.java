package ro.fr33styler.botcreator.bot.protocol;

import ro.fr33styler.botcreator.bot.Bot;

import java.util.logging.Logger;

public interface Protocol {

    Bot newBot(Logger logger, String name);

}
