package com.teamnative.bookon.feature.auth.presentation.component

/**
 * BookOn 계정 비밀번호의 입력 길이와 조합 규칙을 한 곳에서 관리한다.
 * 화면별 입력 상태는 이 정책의 결과를 사용해 오류와 CTA 활성화 여부를 결정한다.
 */
internal object BookOnPasswordPolicy {
    const val MaxLength = 15
    private const val MinLength = 6
    private val validPasswordRegex = Regex(
        "^(?=\\S{$MinLength,$MaxLength}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).*$",
    )

    /** 사용자가 입력한 비밀번호가 서비스 정책을 만족하는지 반환한다. */
    fun isValid(password: String): Boolean = validPasswordRegex.matches(password)

}
