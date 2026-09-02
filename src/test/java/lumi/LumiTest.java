package lumi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests Lumi's responses to graphical-interface input. */
public class LumiTest {
    @Test
    public void getResponse_userInput_lumiEchoReturned() {
        Lumi lumi = new Lumi();

        assertEquals("Lumi heard: read book", lumi.getResponse("read book"));
    }
}
