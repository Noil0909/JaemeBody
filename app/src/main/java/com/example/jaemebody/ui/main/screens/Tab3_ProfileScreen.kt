package com.example.jaemebody.ui.main.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.jaemebody.ui.common.ProfileScreens
import com.example.jaemebody.ui.profile.ProfileEditScreen
import com.example.jaemebody.ui.profile.ProfileInfoScreen


@Composable
fun ProfileScreen(){

    var currentScreen by rememberSaveable { mutableStateOf(ProfileScreens.ProfileInfo)}

    var name by rememberSaveable { mutableStateOf("이름을 입력해주세요.")}
    var age by rememberSaveable {mutableStateOf("0")}
    var height by rememberSaveable {mutableStateOf("0")}

    when (currentScreen){
        ProfileScreens.ProfileInfo -> {
            ProfileInfoScreen(
                name = name,
                age = age,
                height = height,
                onEditClicked = {currentScreen = ProfileScreens.ProfileEdit
                }
            )
        }
        ProfileScreens.ProfileEdit -> {
            ProfileEditScreen(
                initialName = name,
                initialAge = age,
                initialHeight = height,
                onSaveClicked = {newName, newAge, newHeight ->
                    name = newName
                    age = newAge
                    height = newHeight
                    currentScreen = ProfileScreens.ProfileInfo
                },
                onCancelClicked = {
                    currentScreen = ProfileScreens.ProfileInfo
                }
            )
        }
    }
}