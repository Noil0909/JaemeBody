package com.example.jaemebody.ui.dietRecord.dietRecordInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaemebody.repository.FirebaseRepository
import kotlinx.coroutines.launch

class DietRecordInfoViewModel : ViewModel(){

    fun removeExerciseRecord(docId: String){
        viewModelScope.launch {
            FirebaseRepository.removeExercise(docId)
        }
    }
}