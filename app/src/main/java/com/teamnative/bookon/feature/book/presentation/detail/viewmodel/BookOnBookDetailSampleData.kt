package com.teamnative.bookon.feature.book.presentation.detail.viewmodel

internal fun sampleBookDetailUiState(loanAvailable: Boolean) =
    BookOnBookDetailScreenUiState(
        title = "토마토 컵라면",
        author = "차정은",
        libraryNumber = "813.7",
        totalQuantity = 2,
        availableQuantity = if (loanAvailable) 2 else 0,
        intro = "상처와 고민을 안고 살아가는 사람들이 우연한 만남을 통해 서로를 이해하고 위로받는 이야기이다.",
        loanAvailable = loanAvailable,
    )
