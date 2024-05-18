package ro.fr33styler.botcreator.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private final DateFormat dateFormat = new SimpleDateFormat("hh:mm");
    private final Date date = new Date();

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        date.setTime(record.getMillis());
        builder.append(dateFormat.format(date)).append(' ');
        builder.append(record.getLevel()).append(' ');
        builder.append('[').append(record.getLoggerName()).append(']').append(' ');
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}