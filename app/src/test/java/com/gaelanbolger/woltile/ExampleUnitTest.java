package com.gaelanbolger.woltile;

import com.gaelanbolger.woltile.util.NetworkUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will io on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void macString_isCorrect() {
        byte[] bytes = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        String macString = NetworkUtils.MacUtils.getMacString(bytes);
        assertEquals("FF-FF-FF-FF-FF-FF", macString);
    }

    @Test
    public void macBytes_isCorrect() {
        String macString = "FF-FF-FF-FF-FF-FF";
        byte[] macBytes = NetworkUtils.MacUtils.getMacBytes(macString);
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, macBytes);
    }
}