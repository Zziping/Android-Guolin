package com.android.webviewtest

interface HttpCallbackListener {
    fun onFinish(response : String)
    fun onError(e : Exception)
}