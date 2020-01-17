package com.company.watches.utils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record == null ? "" : String.format("[ %s ]%n", record.getMessage());
    }
}
