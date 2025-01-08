package com.example.jaemebody.ui.intro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.jaemebody.ui.intro.screens.LoginScreen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

class IntroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent{
            JaemeBodyTheme{

                LoginScreen()
            }
        }
    }
}