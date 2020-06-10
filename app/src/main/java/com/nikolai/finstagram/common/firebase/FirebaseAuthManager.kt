package com.nikolai.finstagram.common.firebase

import com.nikolai.finstagram.common.AuthManager
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.firebase.common.auth
import com.google.android.gms.tasks.Task

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()
}