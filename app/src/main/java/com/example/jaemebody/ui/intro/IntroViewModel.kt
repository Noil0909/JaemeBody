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

            try{
                val user = FirebaseRepository.signUp(email, password)
                _authState.value = user
                _errorState.value = null
            }
            // 나중에 분기별 예외 처리 ex) timeout ..
            catch (e: Exception){
                _errorState.value = e.message
            }

        }
    }

    // 로그인

    fun signIn(email: String, password: String){
        viewModelScope.launch{

            try{
                val user = FirebaseRepository.signIn(email, password)
                _authState.value = user
                _errorState.value = null
            }
            catch (e: Exception){
                _errorState.value = e.message
            }
        }
    }
}