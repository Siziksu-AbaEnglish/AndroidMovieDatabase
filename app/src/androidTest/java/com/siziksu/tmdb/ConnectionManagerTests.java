package com.siziksu.tmdb;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.siziksu.tmdb.common.manager.ConnectionManager;
import com.siziksu.tmdb.common.model.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public final class ConnectionManagerTests {

    private ConnectionManager connectionManager;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        connectionManager = new ConnectionManager(context);
    }

    @Test
    public void isConnected() throws Exception {
        // WARNING: This test will fail if the device is not connected
        Connection connection = connectionManager.getConnection();
        assertEquals(true, connection.isConnected());
    }
}
