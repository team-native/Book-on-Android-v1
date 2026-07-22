package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.ranking.domain.GetReaderRankingUseCase
import com.teamnative.bookon.feature.ranking.domain.Reader
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingMemberUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Year
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 서버 독서 랭킹을 포디움과 순위 목록 화면 모델로 변환한다. */
@HiltViewModel
class BookOnRankingViewModel @Inject constructor(
    private val getReaderRanking: GetReaderRankingUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(initialRankingUiState())
    val uiState: StateFlow<BookOnRankingScreenUiState> = mutableUiState.asStateFlow()

    init {
        load()
    }

    /** 최초 진입과 재시도에서 해당 연도 랭킹을 다시 조회한다. */
    fun retry() = load()

    private fun load() = viewModelScope.launch {
        mutableUiState.value = mutableUiState.value.copy(errorMessage = null)
        when (val result = getReaderRanking(Year.now().value, RankingLimit)) {
            is NetworkResult.Success -> {
                val members = result.data.readers.map { it.toUiModel() }
                val podiumMembers = (members + List((PodiumSize - members.size).coerceAtLeast(0)) {
                    BookOnRankingMemberUiModel(0, "", "", "")
                }).take(PodiumSize)
                mutableUiState.value = BookOnRankingScreenUiState(
                    description = "${result.data.year}년 · ${result.data.resetPolicy}",
                    podium = BookOnRankingPodiumUiModel(podiumMembers[0], podiumMembers[1], podiumMembers[2]),
                    list = BookOnRankingListUiModel(members.drop(PodiumSize)),
                )
            }

            is NetworkResult.Failure -> mutableUiState.value = initialRankingUiState().copy(
                isInitialLoading = false,
                errorMessage = result.error.toUiMessage(),
            )
        }
    }
}

private fun initialRankingUiState(): BookOnRankingScreenUiState {
    val emptyMember = BookOnRankingMemberUiModel(0, "", "", "")
    return BookOnRankingScreenUiState(
        description = "",
        podium = BookOnRankingPodiumUiModel(emptyMember, emptyMember, emptyMember),
        list = BookOnRankingListUiModel(emptyList()),
        isInitialLoading = true,
    )
}

private fun Reader.toUiModel() = BookOnRankingMemberUiModel(
    rank = rank,
    name = name,
    description = department,
    bookCountText = "${loanCount}권",
)

private fun NetworkError.toUiMessage(): BookOnUiMessage = when (this) {
    is NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(R.string.error_load_ranking)
}

private const val RankingLimit = 20
private const val PodiumSize = 3
