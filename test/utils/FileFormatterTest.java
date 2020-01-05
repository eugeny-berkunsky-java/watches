package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileFormatterTest {
    private FileFormatter formatter;

    @BeforeEach
    void init() {
        formatter = new FileFormatter();
    }

    @Test
    void format() {
        assertEquals("", formatter.format(null));

        final LocalDateTime dateTime = LocalDateTime.parse("2020-01-20T10:15:30");
        LogRecord record = new LogRecord(Level.INFO, "message");
        record.setMillis(dateTime.toEpochSecond(ZoneOffset.ofHours(2)) * 1000);
        record.setLoggerName("logger name");

        assertEquals("20.01.2020 10:15:30.000 | INFO | logger name | message | " + System.lineSeparator(),
                formatter.format(record));
    }
}