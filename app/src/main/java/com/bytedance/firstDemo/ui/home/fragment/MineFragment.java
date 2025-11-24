package com.bytedance.firstDemo.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bytedance.firstDemo.R;
import com.bytedance.firstDemo.data.LoginStateManager;
import com.bytedance.firstDemo.data.UserRepository;
import com.bytedance.firstDemo.ui.login.LoginActivity;
import com.bytedance.firstDemo.ui.profile.AvatarSelectionDialog;

public class MineFragment extends Fragment {

    private final String[] itemLabels = {
            "个人信息", "我的收藏", "浏览历史", "设置", "关于我们", "意见反馈"
    };
    private final int[] itemIcons = {
            R.drawable.ic_profile,   //  个人信息
            R.drawable.ic_star,
            R.drawable.ic_feedback,// 我的收藏
            R.drawable.ic_history,   // 浏览历史
            R.drawable.ic_settings,  // 设置
            R.drawable.ic_about,     // 关于我们
                // 意见反馈
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        TextView nickname = view.findViewById(R.id.tvNickname);
        nickname.setText(LoginStateManager.getCurrentNickname(requireContext()));
        ImageView ivAvatar = view.findViewById(R.id.ivUserAvatar);
        UserRepository repo = new UserRepository(requireContext());
        String avatarName = repo.getUserAvatar(requireContext());

        // 判断是否存在头像
        if (avatarName == null || avatarName.isEmpty()) {
            ivAvatar.setImageResource(R.drawable.default_avatar);  // 使用默认头像
        } else {
            // 动态根据字符串找到 drawable
            int resId = getResources().getIdentifier(avatarName, "drawable", requireContext().getPackageName());
            if (resId != 0) {
                ivAvatar.setImageResource(resId);
            } else {
                ivAvatar.setImageResource(R.drawable.default_avatar);
            }
        }

        LinearLayout itemsContainer = view.findViewById(R.id.ll_items_container);
        for (int i = 0; i < itemsContainer.getChildCount(); i++) {
            View itemView = itemsContainer.getChildAt(i);
            TextView tvItem = itemView.findViewById(R.id.tvItemLabel);
            ImageView ivIcon = itemView.findViewById(R.id.ivItemIcon);
            tvItem.setText(itemLabels[i]);
            ivIcon.setImageResource(itemIcons[i]);
            int finalI = i;
            itemView.setOnClickListener(v ->
                    Toast.makeText(requireContext(), "点击了 " + itemLabels[finalI], Toast.LENGTH_SHORT).show()
            );
        }

        ivAvatar.setOnClickListener(v -> {
            AvatarSelectionDialog dialog =
                    new AvatarSelectionDialog(requireContext(), avatar -> {

                        // 保存到数据库
                        repo.updateUserAvatar(avatar, requireContext());

                        // 更新 UI
                        int resId = getResources().getIdentifier(avatar, "drawable", requireContext().getPackageName());
                        ivAvatar.setImageResource(resId);

                        Toast.makeText(requireContext(), "头像已更新！", Toast.LENGTH_SHORT).show();
                    });

            dialog.show();
        });

        Button logoutButton = view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(v -> {
            LoginStateManager.setLoggedIn(requireContext(), false);
            Toast.makeText(requireContext(), "已退出登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });


        Button btnShowDb = view.findViewById(R.id.btnShowDatabaseInfo);
        btnShowDb.setOnClickListener(v -> {
            String info = repo.getCurrentUserInfo(requireContext());

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("数据库信息")
                    .setMessage(info)
                    .setPositiveButton("关闭", null)
                    .show();
        });


        return view;
    }
}
