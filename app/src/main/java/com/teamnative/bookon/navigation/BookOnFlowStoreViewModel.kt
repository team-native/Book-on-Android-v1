package com.teamnative.bookon.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/** 화면 객체 없이 폼 저장소만 구성 변경 동안 유지한다. */
internal class BookOnFlowStoreViewModel : ViewModel() {
    private val stores = mutableMapOf<String, ViewModelStore>()
    var wantsReadingMarathonLink = false

    fun store(key: String): ViewModelStore = stores.getOrPut(key) { ViewModelStore() }

    fun clear(key: String) {
        stores.remove(key)?.clear()
    }

    override fun onCleared() {
        stores.values.forEach { it.clear() }
        stores.clear()
        wantsReadingMarathonLink = false
    }
}
