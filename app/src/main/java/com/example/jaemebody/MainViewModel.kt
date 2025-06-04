package com.example.jaemebody

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.repository.FirebaseRepository
import com.example.jaemebody.repository.FirebaseRepository.saveProfileData
import com.example.jaemebody.repository.FirebaseRepository.uploadProfileImage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel: ViewModel() {

    // 이름 나이 키
    private val _name = MutableStateFlow("이름")
    val name : StateFlow<String> = _name

    private val _age = MutableStateFlow("0")
    val age : StateFlow<String> = _age

    private val _height = MutableStateFlow("0")
    val height : StateFlow<String> = _height

    private val _weight = MutableStateFlow("")
    val weight : StateFlow<String> = _weight

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage : StateFlow<String?> = _errorMessage

    private val _exerciseRecords = MutableStateFlow<List<Exercise>>(emptyList())
    val exerciseRecords: StateFlow<List<Exercise>> = _exerciseRecords

    private val _targetCalorie = MutableStateFlow(1500)
    val targetCalorie: StateFlow<Int> = _targetCalorie

    init {
        loadProfile()
    }

    fun loadProfile(){
        FirebaseRepository.loadProfileData() {name, age, height, weight, imageUrl ->

            val defaultName = "Unknown Name"
            val defaultAge = "Unknown Age"
            val defaultHeight = "Unknown Height"
            val defaultWeight = "Unknown Weight"

            _name.value = name.ifEmpty { defaultName }
            _age.value = age.ifEmpty { defaultAge }
            _height.value = height.ifEmpty { defaultHeight }
            _weight.value = weight.ifEmpty { defaultWeight }
            _profileImageUrl.value = if (!imageUrl.isNullOrEmpty()) imageUrl else null
        }
    }

    fun saveProfile(
        name: String,
        age: String,
        height: String,
        weight: String,
    ) {
        val currentImageUri = profileImageUri.value

        if (currentImageUri != null) {
            // 이미지가 선택되었으면 업로드 후 저장
            FirebaseRepository.uploadProfileImage(currentImageUri) { imageUrl ->
                val urlToSave = imageUrl ?: _profileImageUrl.value
                saveProfileImage(name, age, height, weight, urlToSave)
            }
        } else {
            // 이미지 선택 안되어 있으면 기존 URL 유지
            saveProfileImage(name, age, height, weight, _profileImageUrl.value)
        }
    }
//    fun saveProfile(name: String, age: String, height: String){
//        FirebaseRepository.saveProfileData(name, age, height) { success ->
//            if (success) {
//                _name.value = name
//                _age.value = age
//                _height.value = height
//            } else {
//                // 실패 예외처리
//                _errorMessage.value = "저장 실패"
//            }
//
//        }
//    }

    // 내부용 프로필 저장 로직
    private fun saveProfileImage(
        name: String,
        age: String,
        height: String,
        weight: String,
        imageUrl: String?
    ) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return

        FirebaseRepository.saveProfileData(email, name, age, height, weight, imageUrl) { success ->
            if (success) {
                _name.value = name
                _age.value = age
                _height.value = height
                _weight.value = weight
                _profileImageUrl.value = imageUrl
            } else {
                _errorMessage.value = "프로필 저장에 실패했습니다."
            }
        }
    }


    fun clearErrorMessage(){
        _errorMessage.value = null
    }

    fun saveExerciseRecord(exercise: Exercise){
        viewModelScope.launch{
             FirebaseRepository.createExercise(exercise)
            loadTodayExercises()
        }
    }

    fun loadExercises(){
        viewModelScope.launch{
            val exercises = FirebaseRepository.readExercise()
            _exerciseRecords.value = exercises
        }
    }

    fun loadTodayExercises() {
        viewModelScope.launch {
            val all = FirebaseRepository.readExercise()
            val today = LocalDate.now().toString()
            _exerciseRecords.value = all.filter { it.date == today }
        }
    }

    fun loadAllExercises() {
        viewModelScope.launch {
            _exerciseRecords.value = FirebaseRepository.readExercise()
        }
    }

    fun logout() {
        FirebaseRepository.logout()
    }

    fun setTargetCalorie(value: Int) {
        _targetCalorie.value = value
    }

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    fun setProfileImageUri(uri: Uri?) {
        _profileImageUri.value = uri
    }
}
