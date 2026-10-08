package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.model.BookOnBookDetailInfoItemUiModel

@Composable
fun BookOnBookDetailMetadata(
    libraryNumber: String,
    totalQuantity: Int?,
    availableQuantity: Int?,
    loanAvailable: Boolean,
) {
    val unknown = stringResource(R.string.book_detail_unknown)
    BookOnBookDetailInfoRow(
        items =
            listOf(
                BookOnBookDetailInfoItemUiModel(
                    label = stringResource(R.string.book_library_number),
                    value = libraryNumber.ifBlank { unknown },
                ),
                BookOnBookDetailInfoItemUiModel(
                    label = stringResource(R.string.book_stock_count),
                    value =
                        stringResource(
                            R.string.book_detail_stock_pair,
                            totalQuantity?.let {
                                stringResource(R.string.book_detail_quantity, it)
                            } ?: unknown,
                            availableQuantity?.let {
                                stringResource(R.string.book_detail_quantity, it)
                            } ?: unknown,
                        ),
                ),
                BookOnBookDetailInfoItemUiModel(
                    label = stringResource(R.string.book_loan_availability),
                    value =
                        stringResource(
                            if (loanAvailable) {
                                R.string.loan_available
                            } else {
                                R.string.loan_unavailable
                            },
                        ),
                    highlighted = loanAvailable,
                ),
            ),
    )
}
