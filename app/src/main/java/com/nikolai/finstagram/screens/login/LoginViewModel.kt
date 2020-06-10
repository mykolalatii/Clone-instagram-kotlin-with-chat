package com.nikolai.finstagram.screens.login

import android.app.Application
import android.arch.lifecycle.LiveData
import com.nikolai.finstagram.R
import com.nikolai.finstagram.common.AuthManager
import com.nikolai.finstagram.common.SingleLiveEvent
import com.nikolai.finstagram.screens.common.BaseViewModel
import com.nikolai.finstagram.screens.common.CommonViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.nikolai.finstagram.data.UsersRepository

class LoginViewModel(private val authManager: AuthManager,
                     private val app: Application,
                     private val commonViewModel: CommonViewModel,
                     onFailureListener: OnFailureListener, private val usersRepo : UsersRepository) : BaseViewModel(onFailureListener) {
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    val goToHomeScreen: LiveData<Unit> = _goToHomeScreen
    private val _goToRegisterScreen = SingleLiveEvent<Unit>()
    val goToRegisterScreen: LiveData<Unit> = _goToRegisterScreen

    fun onLoginClick(email: String, password: String) {
        if (validate(email, password)) {
            authManager.signIn(email, password).addOnSuccessListener {
                usersRepo.setOnlineStatus("true")
                _goToHomeScreen.value = Unit
            }.addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email_and_password))
        }
    }

    private fun validate(email: String, password: String) =
            email.isNotEmpty() && password.isNotEmpty()

    fun onRegisterClick() {
        _goToRegisterScreen.call()
    }
}