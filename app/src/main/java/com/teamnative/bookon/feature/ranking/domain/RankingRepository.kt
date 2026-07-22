package com.teamnative.bookon.feature.ranking.domain

import com.teamnative.bookon.core.network.NetworkResult

interface RankingRepository { suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> }
data class ReaderRanking(val year: Int, val resetPolicy: String, val readers: List<Reader>)
data class Reader(val rank: Int, val name: String, val department: String, val loanCount: Int)
