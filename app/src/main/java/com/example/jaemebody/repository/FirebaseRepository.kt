package com.example.jaemebody.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.jaemebody.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirebaseRepository {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    // 현재 유저 정의. 프로필에 여러 유저를 정의할건데, 그에 대한 고유 키값
    // get()은 실시간으로 최신 인스턴스 가져옴
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser


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

    fun logout() {
        FirebaseAuth.getInstance().signOut()

        // 카카오 로그아웃도 함께 처리
//        UserApiClient.instance.logout { error ->
//            if (error != null) {
//                Log.e("LOGOUT", "Kakao logout failed", error)
//            } else {
//                Log.d("LOGOUT", "Kakao logout succeeded")
//            }
//        }
    }

    // 유저 정보 저장
    fun saveProfileData(
        email: String,
        name: String,
        age: String,
        height: String,
        imageUrl: String? = null,
        onComplete: (Boolean) -> Unit
    ){
        val profile = hashMapOf(
            "email" to email,
            "name" to name,
            "age" to age,
            "height" to height
        )

        imageUrl?.let {
            profile["imageUrl"] = it
        }

        currentUser?.let{ user ->
            db.collection("profiles")
                .document(user.uid)
                .set(profile)
                .addOnSuccessListener {onComplete(true)}
                .addOnFailureListener {onComplete(false)}
        } ?: onComplete(false)
    }

    // 유저 정보 불러오기
    fun loadProfileData(onComplete: (String, String, String, String?) -> Unit) {
        currentUser?.let{ user ->
            db.collection("profiles")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()){
                        val name = document.getString("name") ?: ""
                        val age = document.getString("age") ?: ""
                        val height = document.getString("height") ?: ""
                        val imageUrl = document.getString("imageUrl")
                        onComplete(name, age, height, imageUrl)
                    } else{
                        onComplete("", "", "", null)
                    }
                }
                .addOnFailureListener {
                    onComplete("", "", "", null)
                }
        } ?: onComplete("", "", "", null)
    }

    fun findEmailByNameAndAge(name: String, age: String, onResult: (String?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("profiles")
            .whereEqualTo("name", name)
            .whereEqualTo("age", age)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val email = querySnapshot.documents.first().getString("email")
                    onResult(email)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun sendPasswordResetEmail(email: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    // 운동 기록 만들기
    suspend fun createExercise(exercise: Exercise){

        currentUser?.let{ user ->
            db.collection("diet_records")
                .document(user.uid)
                .collection("exercises")
                .add(exercise)
                .await()
        }
    }

    // 운동 기록 불러오기
    suspend fun readExercise(): List<Exercise> {

        return currentUser?.let { user->
            val documents = db.collection("diet_records")
                .document(user.uid)
                .collection("exercises")
                .get()
                .await()
            documents.documents.mapNotNull{ document->
                document.toObject(Exercise::class.java)?.copy(docId = document.id)
            }
        } ?: emptyList()
    }

    // 운동 기록 삭제하기
    suspend fun removeExercise(documentId: String){
        currentUser?.let { user->
            db.collection("diet_records")
                .document(user.uid)
                .collection("exercises")
                .document(documentId)
                .delete()
                .await()

        }
    }

    // 프로필 이미지 업로드
    // 요금제 변경으로 Firebase에 프로필 업로드 안됨
    fun uploadProfileImage(context: Context, uri: Uri?, onResult: (String?) -> Unit) {
        if (uri == null) {
            Log.e("UPLOAD", "Uri is null")
            onResult(null)
            return
        }

        val filename = UUID.randomUUID().toString() + ".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("profileImages/$filename")

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("UPLOAD", "InputStream is null. Cannot access image.")
                onResult(null)
                return
            }

            storageRef.putStream(inputStream)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        Log.d("UPLOAD", "Upload Success: $downloadUri")
                        onResult(downloadUri.toString())
                    }.addOnFailureListener { e ->
                        Log.e("UPLOAD", "Download URL Failed: ${e.message}")
                        onResult(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UPLOAD", "Upload Failed: ${e.message}")
                    onResult(null)
                }
        } catch (e: Exception) {
            Log.e("UPLOAD", "Exception: ${e.message}")
            onResult(null)
        }
    }
}
