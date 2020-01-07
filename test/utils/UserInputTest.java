package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInputTest {

    private PrintStream savedOut = System.out;
    private ByteArrayOutputStream stream;

    @BeforeEach
    void init() {
        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(savedOut);
    }

    @Test
    void getNumber() {
        BufferedReader reader = new BufferedReader(new CharArrayReader("123".toCharArray()));


        UserInput input = new UserInput(reader);
        assertEquals(123, input.getNumber("", -1));

        //
        reader = new BufferedReader(new CharArrayReader("abc".toCharArray()));
        input = new UserInput(reader);
        assertEquals(-1, input.getNumber("", -1));
    }

    @Test
    void getString() {
        BufferedReader reader = new BufferedReader(new CharArrayReader(" abcd efg ".toCharArray()));
        UserInput input = new UserInput(reader);
        assertEquals("abcd efg", input.getString(""));
    }

    @Test
    void testOutput() {
        BufferedReader reader = new BufferedReader(new CharArrayReader("abc".toCharArray()));
        UserInput input = new UserInput(reader);

        input.getNumber("some message", -1);
        assertEquals("some message: ", stream.toString());
    }
}