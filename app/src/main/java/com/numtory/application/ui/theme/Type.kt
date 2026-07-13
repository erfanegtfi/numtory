package com.eterex.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.numtory.application.R
import com.numtory.application.ui.theme.textBlack
import com.numtory.application.ui.theme.textGray
import com.numtory.application.ui.theme.textGrayDark
import com.numtory.application.ui.theme.textWhite

val CustomFontFamily = FontFamily(
    Font(R.font.vazirmatn_light, FontWeight.Light),
    Font(R.font.vazirmatn_medium, FontWeight.Medium),
    Font(R.font.vazirmatn_regular, FontWeight.Normal),
    Font(R.font.vazirmatn_bold, FontWeight.Bold)
)

//val vazirBold = FontFamily(
//    Font(R.font.vazirmatn_bold)
//)
//val vazirMedium = FontFamily(
//    Font(R.font.vazirmatn_medium)
//)
//val vazirRegular = FontFamily(
//    Font(R.font.vazirmatn_regular)
//)
//val vazirLight = FontFamily(
//    Font(R.font.vazirmatn_light)
//)

private val defaultTypography = Typography()

// Set of Material typography styles to start with
val lightTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    titleLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    titleMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    titleSmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    bodyLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    bodyMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    bodySmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontStyle = FontStyle.Normal,
        color = textBlack,
    ),
    labelLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontStyle = FontStyle.Normal,
        color = textGray,
    ),
    labelMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        fontStyle = FontStyle.Normal,
        color = textGray,
    ),
    labelSmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        fontStyle = FontStyle.Normal,
        color = textGray,
    )

)

val darkTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    titleLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    titleMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    titleSmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    bodyLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    bodyMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    bodySmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontStyle = FontStyle.Normal,
        color = textWhite,
    ),
    labelLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontStyle = FontStyle.Normal,
        color = textGrayDark,
    ),
    labelMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        fontStyle = FontStyle.Normal,
        color = textGrayDark,
    ),
    labelSmall = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        fontStyle = FontStyle.Normal,
        color = textGrayDark,
    )
)