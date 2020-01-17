package utils;

import com.company.watches.utils.ConsoleFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleFormatterTest {

    private ConsoleFormatter formatter;

    @BeforeEach
    void init() {
        formatter = new ConsoleFormatter();
    }

    @Test
    void format() {
        assertEquals("", formatter.format(null));

        LogRecord record = new LogRecord(Level.INFO, "message");
        assertEquals("[ message ]" + System.lineSeparator(), formatter.format(record));
    }
}