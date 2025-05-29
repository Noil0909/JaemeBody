package com.example.jaemebody.ui.common

import com.example.jaemebody.R


// 메인 화면 NAV 탭
enum class JaemeBodyScreen(val icon: Int){
    Home(R.drawable.tab1_icon),  // 홈 화면
    Diet(R.drawable.tab2_icon),  // 다이어트 화면
    Calendar(R.drawable.tab3_icon), // 달력 화면
    Profile(R.drawable.tab3_icon) // 프로필 화면
}

// 프로필 화면 스크린들
enum class ProfileScreens{
    ProfileInfo,
    ProfileEdit
}

// 운동 기록 스크린
enum class DietRecordScreens{
    DietRecordInfo,
    DietRecordAdd
}