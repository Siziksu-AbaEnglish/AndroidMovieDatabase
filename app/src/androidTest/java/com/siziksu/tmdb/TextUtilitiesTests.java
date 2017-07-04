package com.siziksu.tmdb;

import com.siziksu.tmdb.common.utils.TextUtilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TextUtilitiesTests {

    @Test
    public void testingFromHtml() throws Exception {
        String html = "<html><body><p>Hello</p></body></html>";
        String expected = "Hello";
        String actual = TextUtilities.fromHtml(html).toString().replace("\n", "");
        assertEquals(expected, actual);
    }
}
