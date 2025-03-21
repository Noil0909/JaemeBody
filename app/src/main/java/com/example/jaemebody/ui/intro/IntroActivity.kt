package com.example.jaemebody.ui.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jaemebody.MainActivity
import com.example.jaemebody.ui.intro.screens.LoginScreen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

class IntroActivity : ComponentActivity() {

    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("INTRO_DEBUG", "✅ IntroActivity 진입 성공")

        installSplashScreen()

        setContent{
            JaemeBodyTheme{
                val context = LocalContext.current

                val user by introViewModel.authState.collectAsStateWithLifecycle()
                val error by introViewModel.errorState.collectAsStateWithLifecycle()

                Log.d("IntroActivity", "user : $user")

                if(user == null){
                    // 로그인, 회원가입 X
                    Log.d("UI_DEBUG", "로그인 화면 표시 시작")
                    LoginScreen(
                        onLogin = {email, password ->
                            Log.d("AUTH", "로그인 시도: $email")
                            introViewModel.signIn(email, password)
                        },
                        onSignUp = {email, password ->
                            introViewModel.signUp(email, password)
                        },
                        errorMessage = error
                    )
                }
                else{
                    // 로그인, 회원가입 O
                    // 메인 화면으로 이동
                    Log.d("AUTH", "로그인 성공, MainActivity로 이동")
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}