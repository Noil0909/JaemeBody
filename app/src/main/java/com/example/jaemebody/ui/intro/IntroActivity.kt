package com.example.jaemebody.ui.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.example.jaemebody.MainActivity
import com.example.jaemebody.repository.FirebaseRepository
import com.example.jaemebody.ui.intro.screens.LoginScreen
import com.example.jaemebody.ui.intro.screens.RecoverAccountScreen
import com.example.jaemebody.ui.intro.screens.SignUpStep1Screen
import com.example.jaemebody.ui.intro.screens.SignUpStep2Screen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

@ExperimentalWearMaterialApi
@ExperimentalLayoutApi
class IntroActivity : ComponentActivity() {

    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 앱 상단 상태바 숨기기
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        actionBar?.hide()

        installSplashScreen()

        setContent {
            JaemeBodyTheme {
                val context = LocalContext.current

                val user by introViewModel.authState.collectAsStateWithLifecycle()
                val error by introViewModel.errorState.collectAsStateWithLifecycle()

                var screenState by rememberSaveable { mutableStateOf("login") }
                var tempEmail by rememberSaveable { mutableStateOf("") }
                var tempPassword by rememberSaveable { mutableStateOf("") }

                // 로그인 성공 시 MainActivity로 이동
                LaunchedEffect(user) {
                    if (user != null) {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }
                }

                if (user == null) {
                    when (screenState) {
                        "login" -> {
                            LoginScreen(
                                onLogin = { email, password ->
                                    introViewModel.signIn(email, password)
                                },
                                onSignUp = {
                                    screenState = "signup1"
                                },
                                onRecover = {
                                    screenState = "recover" //
                                },
                                errorMessage = error
                            )
                        }
                        "recover" -> {
                            RecoverAccountScreen(
                                onBack = {
                                    screenState = "login"
                                }
                            )
                        }
                        "signup1" -> {
                            SignUpStep1Screen(
                                onNext = { email, password ->
                                    tempEmail = email
                                    tempPassword = password
                                    screenState = "signup2"
                                },
                                onBackToLogin = {
                                    screenState = "login"
                                },
                                errorMessage = error
                            )
                        }

                        "signup2" -> {
                            SignUpStep2Screen(
                                email = tempEmail,
                                password = tempPassword,
                                onRegister = { email, password, name, age, height, weight ->
                                    // 회원가입 후 프로필 저장
                                    introViewModel.registerWithProfile(
                                        email = email,
                                        password = password,
                                        name = name,
                                        age = age,
                                        height = height,
                                        weight = weight,
                                        onFail = {
                                            Toast.makeText(context, "회원가입 또는 저장 실패", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                },
                                onBack = {
                                    screenState = "signup1"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}