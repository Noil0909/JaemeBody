package com.example.jaemebody.util

import com.google.firebase.auth.FirebaseAuthException

object ErrorMessageUtil {
    fun getErrorMessage(exception: Exception?): String {
        return when ((exception as? FirebaseAuthException)?.errorCode) {
            "ERROR_INVALID_EMAIL" -> "유효하지 않은 이메일 주소입니다."
            "ERROR_WRONG_PASSWORD" -> "잘못된 비밀번호입니다."
            "ERROR_USER_NOT_FOUND" -> "사용자를 찾을 수 없습니다."
            "ERROR_USER_DISABLED" -> "사용자가 비활성화되었습니다."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "이미 사용 중인 이메일입니다."
            "ERROR_OPERATION_NOT_ALLOWED" -> "해당 작업이 허용되지 않습니다."
            "ERROR_WEAK_PASSWORD" -> "비밀번호가 너무 약합니다."
            else -> "알 수 없는 오류가 발생했습니다."
        }
    }
}