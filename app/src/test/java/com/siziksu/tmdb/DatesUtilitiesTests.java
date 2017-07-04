package com.siziksu.tmdb;

import android.text.TextUtils;
import android.util.Log;

import com.siziksu.tmdb.common.utils.DatesUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class, Log.class})
public final class DatesUtilitiesTests {

    @Before
    public void init() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence sequence = (CharSequence) invocation.getArguments()[0];
                return !(sequence != null && sequence.length() > 0);
            }
        });
    }

    @Test
    public void year2017IsCorrect() throws Exception {
        String date = "2017-02-16";
        assertEquals("2017", DatesUtilities.getYear(date));
    }

    @Test
    public void yearNotKnownIsCorrect() throws Exception {
        String date = "";
        assertEquals("N/A", DatesUtilities.getYear(date));
    }
}
