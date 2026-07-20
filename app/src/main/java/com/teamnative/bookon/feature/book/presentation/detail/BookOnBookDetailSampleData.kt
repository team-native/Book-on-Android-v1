package com.teamnative.bookon.feature.book.presentation.detail

import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoItemUiModel
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoRowUiModel

internal fun sampleBookDetailUiState(loanAvailable: Boolean) = BookOnBookDetailScreenUiState(
    title = "토마토 컵라면",
    author = "차정은",
    info = BookOnBookDetailInfoRowUiModel(
        items = listOf(
            BookOnBookDetailInfoItemUiModel("도서관 번호", "000"),
            BookOnBookDetailInfoItemUiModel("재고 수량", if (loanAvailable) "2권" else "0권"),
            BookOnBookDetailInfoItemUiModel(
                label = "대출 여부",
                value = if (loanAvailable) "가능" else "불가",
                highlighted = loanAvailable,
            ),
        ),
    ),
    intro = "상처와 고민을 안고 살아가는 사람들이 우연한 만남을 통해 서로를 이해하고 위로받는 과정을 그린 이야기이다.",
    loanAvailable = loanAvailable,
)
