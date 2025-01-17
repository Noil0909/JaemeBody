package com.example.jaemebody.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaemebody.repository.FirebaseRepository
import com.example.jaemebody.util.ErrorMessageUtil.getErrorMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    // 회원가입
    fun signUp(email: String, password: String){
        viewModelScope.launch{

            val user = FirebaseRepository.signUp(email, password)
            _authState.value = user
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener{ task ->
//                    if(task.isSuccessful){
//                        _authState.value = auth.currentUser
//                    } else{
//                        _errorState.value = getErrorMessage(task.exception)
//                    }
//                }
        }
    }

    // 로그인

    fun signIn(email: String, password: String){
        viewModelScope.launch{

            val user = FirebaseRepository.signIn(email, password)
            _authState.value = user
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener{ task ->
//                    if(task.isSuccessful){
//                        _authState.value = auth.currentUser
//                    } else{
//                        _errorState.value = getErrorMessage(task.exception)
//                    }
//                }
        }
    }
}