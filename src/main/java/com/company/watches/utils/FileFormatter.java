package com.company.watches.utils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record == null ? "" : String.format("%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS.%1$tL | " +
                        "%2$s | %3$s | %4$s | %5$s%n",
                record.getMillis(), record.getLevel().getName(), record.getLoggerName(),
                record.getMessage(), record.getThrown() == null ? "" : record.getThrown());
    }
}
