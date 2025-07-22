package com.example.bookingapp.view.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.view.adapter.ImageAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private int[] imageResources;
    private OnImageSelectedListener listener; // Interface để callback

    // Interface để thông báo khi ảnh được chọn
    public interface OnImageSelectedListener {
        void onImageSelected(int imageResourceId);
    }

    // Setter để truyền listener từ Activity
    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }

    public static ImageBottomSheetFragment newInstance(int[] imageResources) {
        ImageBottomSheetFragment fragment = new ImageBottomSheetFragment();
        Bundle args = new Bundle();
        args.putIntArray("imageResources", imageResources);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_bottom_sheet, container, false);

        imageResources = getArguments().getIntArray("imageResources");

        recyclerView = rootView.findViewById(R.id.imageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ImageAdapter adapter = new ImageAdapter(imageResources);
        recyclerView.setAdapter(adapter);

        // Gán sự kiện click từ adapter
        adapter.setOnItemClickListener(imageResourceId -> {
            if (listener != null) {
                listener.onImageSelected(imageResourceId); // Gọi callback khi ảnh được chọn
                dismiss(); // Đóng BottomSheet sau khi chọn
            }
        });

        return rootView;
    }
}