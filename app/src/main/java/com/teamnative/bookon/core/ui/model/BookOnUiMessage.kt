package com.teamnative.bookon.core.ui.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** 화면에서 고정 리소스 문구와 서버의 동적 문구를 같은 방식으로 표시한다. */
sealed interface BookOnUiMessage {
    data class Dynamic(
        val value: String,
    ) : BookOnUiMessage

    data class Resource(
        @param:StringRes val resId: Int,
        val formatArgs: List<Any> = emptyList(),
    ) : BookOnUiMessage
}

/** Composable이 ViewModel의 메시지 종류에 맞는 화면 문구를 해석한다. */
@Composable
fun BookOnUiMessage.resolve(): String = when (this) {
    is BookOnUiMessage.Dynamic -> value
    is BookOnUiMessage.Resource -> stringResource(resId, *formatArgs.toTypedArray())
}
