package com.squirtles.domain.usecase.user

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor() {
    operator fun invoke() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // TODO DB에서 해당 유저와 관련된 모든 정보 지우기

        // 정상적으로 지운 후 유저 삭제
        currentUser?.delete()
    }
}
