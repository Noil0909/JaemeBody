package com.example.jaemebody

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.repository.FirebaseRepository
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
        FirebaseRepository.loadProfileData() {name, age, height ->

            val defaultName = "Unknown Name"
            val defaultAge = "Unknown Age"
            val defaultHeight = "Unknown Height"

            _name.value = name.ifEmpty { defaultName }
            _age.value = age.ifEmpty { defaultAge }
            _height.value = height.ifEmpty { defaultHeight }
        }
    }

    fun saveProfile(name: String, age: String, height: String){
        FirebaseRepository.saveProfileData(name, age, height) { success ->
            if (success) {
                _name.value = name
                _age.value = age
                _height.value = height
            } else {
                // 실패 예외처리
                _errorMessage.value = "저장 실패"
            }

        }
    }

    fun clearErrorMessage(){
        _errorMessage.value = null
    }

    fun saveExerciseRecord(exercise: Exercise){
        viewModelScope.launch{
             FirebaseRepository.createExercise(exercise)
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
}
