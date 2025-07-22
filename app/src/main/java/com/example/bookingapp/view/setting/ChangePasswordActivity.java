package com.example.bookingapp.view.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.repository.UserRepository;
import com.example.bookingapp.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private UserRepository userRepository;
    private ShareReferenceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Khởi tạo View Binding
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Xử lý padding cho system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo UserRepository và SharedPreferences
        userRepository = new UserRepository(this); // Khởi tạo theo cách của bạn
        helper = new ShareReferenceHelper(this);

        // Xử lý nút Back
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity và quay lại màn hình trước
            }
        });

        // Xử lý nút Save
        binding.savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = binding.oldPassword.getText().toString().trim();
                String newPass = binding.newPassword.getText().toString().trim();
                String confirmPass = binding.confirmNewPassword.getText().toString().trim();

                // Kiểm tra cơ bản
                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!helper.get("user", User.class).getPassword().equals(oldPass)){
                    Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(ChangePasswordActivity.this, "New password and confirmation do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass.length() < 6) {
                    Toast.makeText(ChangePasswordActivity.this, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPass.equals(oldPass)){
                    Toast.makeText(ChangePasswordActivity.this, "Please use a different password than your current password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy userId từ SharedPreferences
                User u = helper.get("user", User.class);
                if (u == null) {
                    Toast.makeText(ChangePasswordActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPass.equals(u.getPassword())) {
                    Toast.makeText(ChangePasswordActivity.this, "Please use a different password than your current password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                u.setPassword(newPass);
                // Gọi phương thức thay đổi mật khẩu từ UserRepository
                boolean result = userRepository.updateUser(u);
                if (result) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    // Clear các trường EditText
                    binding.oldPassword.setText("");
                    binding.newPassword.setText("");
                    binding.confirmNewPassword.setText("");
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Giải phóng Binding để tránh memory leak
    }
}