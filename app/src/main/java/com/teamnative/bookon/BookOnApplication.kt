package com.teamnative.bookon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** 앱 전체 의존성 그래프를 초기화한다. */
@HiltAndroidApp
class BookOnApplication : Application()
