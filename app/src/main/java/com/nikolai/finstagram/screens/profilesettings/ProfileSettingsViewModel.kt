package com.nikolai.finstagram.screens.profilesettings

import com.nikolai.finstagram.common.AuthManager
import com.nikolai.finstagram.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class ProfileSettingsViewModel(private val authManager: AuthManager,
                               onFailureListener: OnFailureListener) :
        BaseViewModel(onFailureListener),
        AuthManager by authManager