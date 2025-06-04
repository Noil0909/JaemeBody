package com.example.jaemebody.ui.main.screens

import android.widget.Toast
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.ui.common.ProfileScreens
import com.example.jaemebody.ui.profile.ProfileEditScreen
import com.example.jaemebody.ui.profile.ProfileInfoScreen


@Composable
@ExperimentalWearMaterialApi
@ExperimentalLayoutApi
fun ProfileScreen(mainViewModel: MainViewModel) {
    // profile screen 탭 로드 되고 MainViewModel의 loadProfile() 실행하는 방법도 있다
    // 이러한 경우 이 코드에서 mainViewModel.loadProfile() 23번줄에 호출해 로드 실행
    var currentScreen by rememberSaveable { mutableStateOf(ProfileScreens.ProfileInfo)}

    val context = LocalContext.current

    val name by mainViewModel.name.collectAsState()
    val age by mainViewModel.age.collectAsState()
    val height by mainViewModel.height.collectAsState()
    val weight by mainViewModel.weight.collectAsState()
    val imageUri by mainViewModel.profileImageUri.collectAsState()

    val errorMessage by mainViewModel.errorMessage.collectAsState()

    if(errorMessage != null){
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
        mainViewModel.clearErrorMessage()
    }

    when (currentScreen){
        ProfileScreens.ProfileInfo -> {
            ProfileInfoScreen(
                mainViewModel,
                name = name,
                age = age,
                height = height,
                weight = weight,
                onEditClicked = {currentScreen = ProfileScreens.ProfileEdit
                }
            )
        }
        ProfileScreens.ProfileEdit -> {
            ProfileEditScreen(
                initialName = name,
                initialAge = age,
                initialHeight = height,
                initialWeight = weight,
                onSaveClicked = { newName, newAge, newHeight, newWeight ->
                    mainViewModel.saveProfile(newName, newAge, newHeight, newWeight)
                    currentScreen = ProfileScreens.ProfileInfo
                },
                onCancelClicked = {
                    currentScreen = ProfileScreens.ProfileInfo
                }
            )
        }
    }
}