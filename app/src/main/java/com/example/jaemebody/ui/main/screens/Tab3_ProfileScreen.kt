package com.example.jaemebody.ui.main.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.jaemebody.ui.profile.ProfileEditScreen
import com.example.jaemebody.ui.profile.ProfileInfoScreen

enum class Screen{
    ProfileInfo,
    ProfileEdit
}

@Composable
fun ProfileScreen(){

    var currentScreen by rememberSaveable { mutableStateOf(Screen.ProfileInfo)}

    var name by rememberSaveable { mutableStateOf("이름을 입력해주세요.")}
    var age by rememberSaveable {mutableStateOf("0")}
    var height by rememberSaveable {mutableStateOf("0")}

    when (currentScreen){
        Screen.ProfileInfo -> {
            ProfileInfoScreen(
                name = name,
                age = age,
                height = height,
                onEditClicked = {currentScreen = Screen.ProfileEdit
                }
            )
        }
        Screen.ProfileEdit -> {
            ProfileEditScreen(
                initialName = name,
                initialAge = age,
                initialHeight = height,
                onSaveClicked = {newName, newAge, newHeight ->
                    name = newName
                    age = newAge
                    height = newHeight
                    currentScreen = Screen.ProfileInfo
                },
                onCancelClicked = {
                    currentScreen = Screen.ProfileInfo
                }
            )
        }
    }
}