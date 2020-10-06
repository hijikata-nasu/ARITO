package com.nitok_ict.strawberrypie.arito

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented face_image_0, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under face_image_0.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nitok_ict.strawberrypie.arito", appContext.packageName)
    }
}