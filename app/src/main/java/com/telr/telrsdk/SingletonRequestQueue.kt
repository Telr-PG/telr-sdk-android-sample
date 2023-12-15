package com.telr.telrsdk

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class SingletonRequestQueue private constructor(private val mContext: Context) {
    private var mRequestQueue: RequestQueue?
    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mContext)
            }
            return mRequestQueue
        }

    companion object {
        private var mInstance: SingletonRequestQueue? = null
        @Synchronized
        fun getInstance(context: Context): SingletonRequestQueue? {
            if (mInstance == null) {
                mInstance = SingletonRequestQueue(context)
            }
            return mInstance
        }
    }

    init {
        mRequestQueue = requestQueue
    }
}