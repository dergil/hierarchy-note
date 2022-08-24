package com.github.dergil.hierarchynote.view.activities.ui.login;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.github.dergil.hierarchynote.HierarchyApp;
import com.github.dergil.hierarchynote.view.activities.data.LoginDataSource;
import com.github.dergil.hierarchynote.view.activities.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application mApplication;

    private final  LoginRepository mRepository;

    public LoginViewModelFactory(@NonNull Application application) {
        mApplication = application;
        mRepository = ((HierarchyApp) application).getLoginRepository();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, mRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}