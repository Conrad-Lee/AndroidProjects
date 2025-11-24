package com.bytedance.firstDemo.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.bytedance.firstDemo.R;

public class AvatarSelectionDialog extends Dialog {

    private ImageView ivAvatar1, ivAvatar2, ivAvatar3;
    private AvatarSelectListener listener;

    public AvatarSelectionDialog(@NonNull Context context, AvatarSelectListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_avatar_selection);

        ivAvatar1 = findViewById(R.id.ivAvatar1);
        ivAvatar2 = findViewById(R.id.ivAvatar2);
        ivAvatar3 = findViewById(R.id.ivAvatar3);

        ivAvatar1.setOnClickListener(v -> selectAvatar("avatar1"));
        ivAvatar2.setOnClickListener(v -> selectAvatar("avatar2"));
        ivAvatar3.setOnClickListener(v -> selectAvatar("avatar3"));
    }

    private void selectAvatar(String avatar) {
        listener.onAvatarSelected(avatar);
        dismiss();
    }

    public interface AvatarSelectListener {
        void onAvatarSelected(String avatar);
    }
}
