package com.comp.iitb.vialogue;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.NotificationManagerCompat;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(appContext);
        Iterator<String> iterator = packageNames.iterator();
        while (iterator.hasNext()) {
            System.out.println("herere---------------------------------------------" + iterator.next());
        }
        assertEquals("com.comp.iitb.vialogue", appContext.getPackageName());
    }
}
