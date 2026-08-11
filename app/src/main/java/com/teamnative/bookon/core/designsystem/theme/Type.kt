package com.teamnative.bookon.core.designsystem.theme

import androidx.compose.material3.Typography as MaterialTypography
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.teamnative.bookon.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_thin, weight = FontWeight.Thin),
    Font(R.font.pretendard_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.pretendard_light, weight = FontWeight.Light),
    Font(R.font.pretendard_regular, weight = FontWeight.Normal),
    Font(R.font.pretendard_medium, weight = FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.pretendard_black, weight = FontWeight.Black),
)

private val DefaultTypography = MaterialTypography()

// 앱 전체 Material3 텍스트 스타일이 Pretendard weight 매핑을 사용하도록 설정한다.
val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = PretendardFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = PretendardFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = PretendardFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = PretendardFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = PretendardFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = PretendardFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = PretendardFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = PretendardFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = PretendardFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = PretendardFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = PretendardFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = PretendardFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = PretendardFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = PretendardFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = PretendardFontFamily),
)

object BookOnTypography {
    val screenTitle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    )
    val homeUserName = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 20.sp,
    )
    val sectionTitle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 22.sp,
    )
    val topBarTitle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    val button = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    )
    val fieldLabel = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    )
    val fieldText = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
    val fieldPlaceholder = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    )
    val bodyMedium = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )
    val bodySemiBold = bodyMedium.copy(fontWeight = FontWeight.SemiBold)

    val caption = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
    )
    val privacyNotice = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 10.sp,
    )
    val chip = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    )
    val badge = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
    )
    val bookTitle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    )
    val bookMeta = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 22.sp,
    )
}
