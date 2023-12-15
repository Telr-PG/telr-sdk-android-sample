package com.telr.telrsdk

import android.content.Context
import android.util.Log
import com.telr.mobile.sdk.TelrApplication

/**
 * Created by staff on 10/30/17.
 */
class DemoApplication : TelrApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Demo", "Context Started....")
        context = getApplicationContext()
    }

    companion object {
        var context: Context? = null
            private set
    }
}