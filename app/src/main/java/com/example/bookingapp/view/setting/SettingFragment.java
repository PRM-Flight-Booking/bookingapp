package com.example.bookingapp.view.setting;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.databinding.FragmentSettingBinding;
import com.example.bookingapp.view.auth.LoginActivity;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private ShareReferenceHelper prefs;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Tải trạng thái ban đầu từ SharedPreferences
        prefs = new ShareReferenceHelper(requireContext());
        Boolean notificationEnabled = prefs.get("notification_enabled", Boolean.class);
        Boolean darkModeEnabled = prefs.get("dark_mode_enabled", Boolean.class);
        if (notificationEnabled != null) binding.notification.setChecked(notificationEnabled);
        if (darkModeEnabled != null) binding.darkMode.setChecked(darkModeEnabled);
        binding.imgUser.setImageResource(prefs.get("user", User.class).getAvatar());
        binding.nameUser.setText(prefs.get("user", User.class).getName());

        // Thiết lập sự kiện click và gửi giá trị Switch
        binding.notification.setOnCheckedChangeListener((buttonView, isChecked) -> onNotificationSettingsClicked(isChecked));
        binding.darkMode.setOnCheckedChangeListener((buttonView, isChecked) -> onDarkModeSettingsClicked(isChecked));
        binding.aboutApp.setOnClickListener(v -> onAboutAppClicked());
        binding.shareApp.setOnClickListener(v -> onShareAppClicked());
        binding.logoutButton.setOnClickListener(v -> onLogoutClicked());
        binding.editProfile.setOnClickListener(v -> onProfileClicked());
        binding.changePassword.setOnClickListener(v -> onEditPasswordClicked());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Các phương thức tương ứng với hành động của người dùng

    public void onNotificationSettingsClicked(boolean isEnabled) {
        prefs.save("notification_enabled", isEnabled); // Lưu trạng thái
        showNotificationSettings(isEnabled);
    }

    public void onDarkModeSettingsClicked(boolean isEnabled) {
        prefs.save("dark_mode_enabled", isEnabled); // Lưu trạng thái
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Bật Dark Mode
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Tắt Dark Mode
        }
        showDarkModeSettings(isEnabled);
    }

    public void onAboutAppClicked() {
        startActivity(new Intent(getContext(), AboutAppActivity.class));
    }

    public void onShareAppClicked() {
        startActivity(new Intent(getContext(), ShareAppActivity.class));
    }

    public void onLogoutClicked() {
        showLogoutConfirmation();
    }

    public void onLogoutConfirmed() {
        prefs.clear(); // Xóa toàn bộ dữ liệu SharedPreferences khi logout
        showLogoutSuccess();
    }

    public void onLogoutCancelled() {
        // Không làm gì khi hủy logout
    }

    public void onProfileClicked() {
        User u = prefs.get("user", User.class);
        showProfile(u);
    }

    public void onEditPasswordClicked() {
        startActivity(new Intent(getContext(), ChangePasswordActivity.class));
    }

    // Phương thức hiển thị thông tin và trạng thái

    public void showProfile(User u) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Làm mới dữ liệu trong SettingFragment
            binding.imgUser.setImageResource(prefs.get("user", User.class).getAvatar());
            binding.nameUser.setText(prefs.get("user", User.class).getName());
        }
    }

    public void showNotificationSettings(boolean isEnabled) {
        binding.notification.setChecked(isEnabled); // Cập nhật trạng thái Switch
        Toast.makeText(getContext(), "Notifications: " + (isEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    public void showDarkModeSettings(boolean isEnabled) {
        binding.darkMode.setChecked(isEnabled); // Cập nhật trạng thái Switch
        Toast.makeText(getContext(), "Dark Mode: " + (isEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    public void showLogoutConfirmation() {
        new AlertDialog.Builder(requireContext()).setTitle("Log Out").setMessage("Are you sure you want to log out?").setPositiveButton("Yes", (dialog, which) -> onLogoutConfirmed()).setNegativeButton("No", (dialog, which) -> onLogoutCancelled()).setCancelable(false).show();
    }

    public void showLogoutSuccess() {
        // Tạo ProgressDialog để hiển thị màn hình loading
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Logging out..."); // Thông điệp hiển thị
        progressDialog.setCancelable(false); // Ngừng hủy khi bấm ra ngoài
        progressDialog.show(); // Hiển thị ProgressDialog

        // Dùng Handler để trì hoãn chuyển Activity sau 3 giây
        new Handler().postDelayed(() -> {
            progressDialog.dismiss();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        }, 3000); // Properly handle delayed operation
    }
}
