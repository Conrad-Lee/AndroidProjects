// 文件位置：com.bytedance.firstDemo.ui.home.HomeActivity
package com.bytedance.firstDemo.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bytedance.firstDemo.R;
import com.bytedance.firstDemo.data.LoginStateManager;
import com.bytedance.firstDemo.data.UserRepository;
import com.bytedance.firstDemo.ui.home.fragment.*;
import com.bytedance.firstDemo.ui.login.LoginActivity;
import com.bytedance.firstDemo.ui.upload.UploadActivity;

public class HomeActivity extends AppCompatActivity {

    private final Fragment home = new HomePageFragment();
    private final Fragment friend = new FriendFragment();
    private final Fragment message = new MessageFragment();
    private final Fragment mine = new MineFragment();
    private ImageView ivUserAvatar;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        initCustomNav();
        boolean toMine = getIntent().getBooleanExtra("navigateToMine", false);
        if (toMine) {
            TextView tvMine = findViewById(R.id.nav_mine);
            tvMine.performClick();
        }else{
            TextView tvMine = findViewById(R.id.nav_home);
            tvMine.performClick();
        }

    }

    private void initCustomNav() {
        TextView tvHome = findViewById(R.id.nav_home);
        TextView tvFriend = findViewById(R.id.nav_friend);
        TextView tvUpload = findViewById(R.id.nav_upload);
        TextView tvMessage = findViewById(R.id.nav_message);
        TextView tvMine = findViewById(R.id.nav_mine);

        View.OnClickListener listener = v -> {
            resetNavText();
            int id = v.getId();
            if (id == R.id.nav_home) {
                showFragment(home);
                highlight(tvHome);
            } else if (id == R.id.nav_friend) {
                showFragment(friend);
                highlight(tvFriend);
            } else if (id == R.id.nav_message) {
                showFragment(message);
                highlight(tvMessage);
            } else if (id == R.id.nav_mine) {
                if (LoginStateManager.isLoggedIn(HomeActivity.this)) {
                    showFragment(mine);
                    highlight(tvMine);
                } else {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        tvHome.setOnClickListener(listener);
        tvFriend.setOnClickListener(listener);
        tvMessage.setOnClickListener(listener);
        tvMine.setOnClickListener(listener);

        // 加号点击：跳转 UploadActivity
        tvUpload.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
            startActivity(intent);
        });

        // 默认首页选中
        tvHome.performClick();
    }

    private void highlight(TextView tv) {
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(null, Typeface.BOLD);
    }

    private void resetNavText() {
        int[] ids = {R.id.nav_home, R.id.nav_friend, R.id.nav_message, R.id.nav_mine};
        for (int id : ids) {
            TextView tv = findViewById(id);
            tv.setTextColor(Color.parseColor("#888888"));
            tv.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
