package com.example.greetingcard

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "aa0d4de155389c64d11681477ffe0ad6")
        KakaoMapSdk.init(this, "737dc405fb22a6291f7f6d97698faa53")
    }
}
