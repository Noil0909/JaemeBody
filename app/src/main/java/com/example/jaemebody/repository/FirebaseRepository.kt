package com.example.jaemebody.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object FirebaseRepository {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    // 회원가입
    suspend fun signUp(email: String, password: String): FirebaseUser?{
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    // 로그인
    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }
}