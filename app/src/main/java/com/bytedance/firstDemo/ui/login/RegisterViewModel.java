// RegisterViewModel.java
package com.bytedance.firstDemo.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bytedance.firstDemo.data.LoginStateManager;
import com.bytedance.firstDemo.data.UserRepository;

public class RegisterViewModel extends AndroidViewModel {
    private final UserRepository repository;
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String account, String password, String nickname) {
        if (!isValidAccount(account)) {
            registerResult.setValue(new RegisterResult(false, "账号格式不合法"));
            return;
        }
        if (password.length() < 6) {
            registerResult.setValue(new RegisterResult(false, "密码至少6位"));
            return;
        }
        if (nickname.isEmpty()) {
            registerResult.setValue(new RegisterResult(false, "昵称不能为空"));
            return;
        }
        if (repository.userExists(account)) {
            registerResult.setValue(new RegisterResult(false, "该账号已注册"));
        } else {
            boolean success = repository.registerUser(account, password, nickname);
            if (success) {
                // 自动登录
                Context context = getApplication().getApplicationContext();
                LoginStateManager.setLoggedIn(context, true);
                SharedPreferences.Editor editor = context.getSharedPreferences("login_state", Context.MODE_PRIVATE).edit();
                editor.putString("nickname", nickname);
                editor.apply();

                registerResult.setValue(new RegisterResult(true, nickname));
            } else {
                registerResult.setValue(new RegisterResult(false, "注册失败"));
            }
        }
    }

    private boolean isValidAccount(String input) {
        return input.matches("^1[3-9]\\d{9}$") || input.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }

    public static class RegisterResult {
        public final boolean success;
        public final String message;

        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}