package com.example.greetingcard

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "aa0d4de155389c64d11681477ffe0ad6")
        KakaoMapSdk.init(this, "LBVpzlH+WOk0lfW6q4/xB/kMp6w=")
    }
}
