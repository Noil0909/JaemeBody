package com.example.jaemebody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jaemebody.ui.main.JaemeBodyScreen
import com.example.jaemebody.ui.main.MainNavigation
import com.example.jaemebody.ui.main.screens.DietScreen
import com.example.jaemebody.ui.main.screens.HomeScreen
import com.example.jaemebody.ui.main.screens.ProfileScreen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaemeBodyTheme {

                var currentScreen by remember {mutableStateOf(JaemeBodyScreen.Home)}

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                ){
                    Box(modifier = Modifier
                        .padding(bottom = 60.dp)
                        .fillMaxSize()){
                        when(currentScreen){
                            JaemeBodyScreen.Home -> HomeScreen()
                            JaemeBodyScreen.Diet -> DietScreen()
                            JaemeBodyScreen.Profile -> ProfileScreen()
                        }
                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    MainNavigation(currentScreen = currentScreen, onTabSelected = {
                        currentScreen = it
                    })
                }
            }
        }
    }
}
